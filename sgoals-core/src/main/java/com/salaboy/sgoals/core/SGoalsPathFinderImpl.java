/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.core;

import com.salaboy.sgoals.builders.StateBuilder;
import com.salaboy.sgoals.model.api.Atom;
import com.salaboy.sgoals.model.api.FluentPredicate;
import com.salaboy.sgoals.model.api.Graph;
import com.salaboy.sgoals.model.api.Layer;
import com.salaboy.sgoals.model.api.Link;
import com.salaboy.sgoals.model.api.Operator;
import com.salaboy.sgoals.model.api.Path;
import com.salaboy.sgoals.model.api.Predicate;
import com.salaboy.sgoals.model.api.SGoalsPathFinder;
import com.salaboy.sgoals.model.api.State;
import com.salaboy.sgoals.model.api.Step;
import com.salaboy.sgoals.model.impl.GraphImpl;
import com.salaboy.sgoals.model.impl.LayerImpl;
import com.salaboy.sgoals.model.impl.LinkImpl;
import com.salaboy.sgoals.model.impl.NoOpOperator;
import com.salaboy.sgoals.model.impl.PathImpl;
import com.salaboy.sgoals.model.impl.StepImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author salaboy
 */
public class SGoalsPathFinderImpl implements SGoalsPathFinder {

    private Graph graph = new GraphImpl();

    private State goalState;

    private Set<Operator> unGroundedOperators;
    
    private Map<Integer, Set<Predicate>> bannedGoalsForLayer = new HashMap<Integer, Set<Predicate>>();
    
    private Path path;
    
    private int layerCount = 0;

    public SGoalsPathFinderImpl(State initialState, Set<Operator> operators, State goalState) {
        this.goalState = goalState;
        this.unGroundedOperators = operators;
        if (!checkForGroundedCollection(initialState.getPossitivePredicates())) {
            throw new IllegalStateException("The initial state predicates must be grounded");
        }
        if(initialState.getNegativePredicates().size() > 0){
                throw new IllegalStateException("The initial state cannot contain negated predicates");
        }
        if (!checkForGroundedCollection(goalState.getPossitivePredicates())) {
            throw new IllegalStateException("The goal state predicates(+) must be grounded");
        }
        if (!checkForGroundedCollection(goalState.getNegativePredicates())) {
            throw new IllegalStateException("The goal state predicates(-) must be grounded");
        }
        

        graph.addLayer(new LayerImpl("initialLayer", null, null, initialState, null, null));

    }

    @Override
    public Path process() {

        /*
         While we don't find the goal and there is no fixed point we expand the graph
         */
        while (!isGoalFulfilled(graph.getLastLayer()) && !reachedFixedPoint(graph)) {
            Layer layer = expand();
            graph.addLayer(layer);
        }

        /*
         Once we have the graph expanded we proceed to extract the steps
         */
        path = new PathImpl(graph.size() -1);
        Set<Predicate> goal = new HashSet();
        goal.addAll(goalStatePredicates);
        
        extract(graph, goal, graph.size() - 1);


        
        return path;
    }
    /*
     *   This method is supposed to take all the current layer and calculate the next one 
     *   iteratively
     */

    private Layer expand() {
        System.out.println("-----------------------------------------");
        System.out.println("Layer : " + graph.getLastLayer().getName());
        Layer layer = graph.getLastLayer();

        // The goal is contained in the layer and the predicates inside it are not in the mutexed list
        // Or we reached a fixed point
        List<Operator> groundedOperators = findAndGroundOperators(layer);

        if (groundedOperators != null) {
            for (Operator o : groundedOperators) {
                System.out.println(" Grounded Operator: " + o.toString());
            }
        }

        // Adding No Op Operators so they can be applied to the Next Predicates
        for (Predicate p : layer.getFluentPredicates()) {
            groundedOperators.add(new NoOpOperator(p));
        }

        Set<Predicate> nextPredicates = new HashSet<Predicate>();
        nextPredicates.addAll(layer.getRigidPredicates());
        // Adding all the possitive effects
        for (Operator o : groundedOperators) {
            nextPredicates.addAll(o.getPossitiveEffects());
        }

        if (nextPredicates != null) {
            for (Predicate p : nextPredicates) {
                System.out.println(" Predicate: " + p.toString());
            }
        }

        Map<Operator, Set<Operator>> mutexedOperators = findMutexedOperators(groundedOperators, layer);

        if (mutexedOperators != null) {
            for (Operator o : mutexedOperators.keySet()) {
                System.out.println(" Mutexed Operator Key: " + o.toString());
                System.out.println(" \t\t Mutexed Operator List: " + mutexedOperators.get(o));
            }
        }

        // Do I need to find here the mutexed predicates?
        Map<Predicate, Set<Predicate>> mutexedPredicates = findMutexedPredicates(groundedOperators, mutexedOperators, nextPredicates);
        if (mutexedPredicates != null) {
            for (Predicate p : mutexedPredicates.keySet()) {
                System.out.println(" Mutexed Predicate Key: " + p.toString());
                System.out.println(" \t\t  Mutexed Predicate List: " + mutexedPredicates.get(p));
            }
        }
        // Creating links
        Set<Link> links = new HashSet<Link>();
        for (Operator op : groundedOperators) {
            for (Predicate p : op.getPreconditions()) {
                links.add(new LinkImpl(op, p, "precondition"));
            }
            for (Predicate p : op.getPossitiveEffects()) {
                links.add(new LinkImpl(op, p, "possitive"));
            }
            for (Predicate p : op.getNegativeEffects()) {
                links.add(new LinkImpl(op, p, "negative"));
            }
        }
        List<Link> linksList = new ArrayList<Link>(links);
        Collections.sort(linksList, new Comparator<Link>() {

            @Override
            public int compare(Link o1, Link o2) {
                return String.CASE_INSENSITIVE_ORDER.compare(o1.getOperator().toString(), o2.getOperator().toString());
            }
        });
        for (Link l : linksList) {
            System.out.println("Link: " + l);
        }

        System.out.println("+---------------------------------------+");

        //Creating and expanding the next layer
        layerCount++;
        return new LayerImpl("layer" + layerCount, groundedOperators, mutexedOperators, nextPredicates, mutexedPredicates, links);

    }

    /*
     * This method purpose is to look through all the operators
     * that can be applied to generate the next layer.
     * Notice that this method also calculate and remove the mutex operators
     */
    private List<Operator> findAndGroundOperators(Layer layer) {
        List<Operator> operatorsForNextLayer = findOperatorsThatApply(layer);
        operatorsForNextLayer = groundOperators(layer, operatorsForNextLayer);

        // Removing conflicting operatords based on Muetexed predicates from the previous layer
        List<Operator> removeOps = new ArrayList<Operator>();
        for (Operator o : operatorsForNextLayer) {
            List<Predicate> preconditions = o.getPreconditions();

            for (int i = 0; i < preconditions.size(); i++) {
                for (int j = 0; j < preconditions.size() - 1; j++) {
                    int count = i + j + 1;
                    if (count >= preconditions.size()) {
                        count = count - preconditions.size();
                    }
                    Predicate pa = preconditions.get(i);
                    Predicate pb = preconditions.get(count);
                    if (layer.getMutexedPredicates() != null) {
                        Set<Predicate> mutexedPa = layer.getMutexedPredicates().get(pa);
                        if (mutexedPa != null) {
                            if (mutexedPa.contains(pb)) {
                                removeOps.add(o);

                            }
                        }
                    }

                }

            }

        }
        for (Operator o : removeOps) {
            operatorsForNextLayer.remove(o);
        }
        return operatorsForNextLayer;

    }

    /*
     * Looking at the possitive pre-conditions of the operators and 
     * the available predicates an initial match is performed by matching
     * the class of the predicate with the one of the pre-condition. If all 
     * the preconditions classes are in the predicates, the operator will match 
     * and it will be processed forward. 
     */
    private List<Operator> findOperatorsThatApply(Layer layer) {
        List<Operator> operatorsForNextLayer = new ArrayList<Operator>();
        for (Operator operator : unGroundedOperators) {
            List<Predicate> possitivePreconditions = operator.getPreconditions();
            List<Predicate> predicates = new ArrayList<Predicate>(layer.getPredicates());
            int count = 0;
            for (Predicate preCondPredicate : possitivePreconditions) {
                for (Predicate predicate : predicates) {
                    if (predicate.getClass().equals(preCondPredicate.getClass())) {
                        count++;
                        break;
                    }
                }
            }
            if (count == possitivePreconditions.size()) {
                operatorsForNextLayer.add(operator);
            }
        }
        return operatorsForNextLayer;
    }

    /*
     * Now that we know that there are enough predicates to match the operator pre-conditions
     * we need to ground the operators and find all the possible grounded combinations.
    
     */
    private List<Operator> groundOperators(Layer layer, List<Operator> operators) {
        List<Operator> groundedOperators = new ArrayList<Operator>();

        List<Atom> atoms = getAtomsFromPredicates(layer.getPredicates());
        for (Operator operator : operators) {
            Class[] appliesTo = operator.appliesTo();
            List<List<Atom>> combinations = findPredicateCombinationsForOperator(appliesTo, atoms);
            for (List<Atom> selectedAtoms : combinations) {
                Operator groundedOperator = operator.ground(selectedAtoms.toArray());
                List<Predicate> operatorPreconditions = groundedOperator.getPreconditions();

                int count = 0;
                for (Predicate op : operatorPreconditions) {
                    for (Predicate p : layer.getPredicates()) {

                        if (p.equals(op)) {
                            count++;
                        }
                    }
                }
                // Does this operator is grounded and can be applied to the state based on its preconditions?
                if (count == operatorPreconditions.size()) {
                    groundedOperators.add(groundedOperator);
                }
            }

        }

        return groundedOperators;

    }

    private Map<Operator, Set<Operator>> findMutexedOperators(List<Operator> groundedOperators, Layer layer) {
        Map<Operator, Set<Operator>> mutexOperators = new HashMap<Operator, Set<Operator>>();
        for (int i = 0; i < groundedOperators.size(); i++) {
            for (int j = 0; j < groundedOperators.size() - 1; j++) {
                int count = i + j + 1;
                if (count >= groundedOperators.size()) {
                    count = count - groundedOperators.size();
                }

                Operator operatorA = groundedOperators.get(i);
                Operator operatorB = groundedOperators.get(count);

                boolean areIndepAB = independent(operatorA, operatorB);
                //Are dependent operators
                if (!areIndepAB) {
                    Set<Operator> o = mutexOperators.get(operatorA);
                    if (o == null) {
                        mutexOperators.put(operatorA, new HashSet<Operator>());
                    }
                    mutexOperators.get(operatorA).add(operatorB);

                }

                if (layer.getMutexedPredicates() != null) {
                    for (Predicate key : layer.getMutexedPredicates().keySet()) {

                        if (operatorA.getPreconditions().contains(key)) {
                            for (Predicate preB : operatorB.getPreconditions()) {
                                if (preB instanceof FluentPredicate) {

                                    if (layer.getMutexedPredicates().get(key).contains(preB)) {
//                                            System.out.println("Checking Mutex Predicates: " + key);
//                                            System.out.println("\t against Predicatee: " + preB);
//                                            System.out.println("Mutex Pair Found: (" + operatorA + " , " + operatorB + ")");

                                        Set<Operator> o = mutexOperators.get(operatorA);
                                        if (o == null) {
                                            mutexOperators.put(operatorA, new HashSet<Operator>());
                                        }
                                        mutexOperators.get(operatorA).add(operatorB);
                                    }
                                }
                            }

                        }
                    }
                }

            }

        }

        return mutexOperators;
    }

    /*
     * This method extract all the grounded atoms from the predicates.
     * Notice that the predicates must be fully grounded in order to extract their atoms.
     */
    private List<Atom> getAtomsFromPredicates(Set<Predicate> predicates) {
        List<Atom> atoms = new ArrayList<Atom>();
        for (Predicate predicate : predicates) {
            if (predicate.isGrounded()) {
                for (Atom a : predicate.getAtoms()) {
                    if (!atoms.contains(a)) {
                        atoms.add(a);
                    }
                }
            } else {
                throw new IllegalStateException("The predicates here must be fully grounded");
            }
        }
        return atoms;
    }

    private List<List<Atom>> findPredicateCombinationsForOperator(Class[] appliesTo, List<Atom> atoms) {
        List<List<Atom>> combinations = new ArrayList<List<Atom>>();

        List<Atom> localAtoms = new ArrayList<Atom>(atoms);
        List<Atom> selectedAtoms = new ArrayList<Atom>();
        selectAtoms(combinations, selectedAtoms, appliesTo, localAtoms);

        return combinations;

    }

    private void selectAtoms(List<List<Atom>> combinations, List<Atom> selectedAtoms, Class[] appliesTo, List<Atom> localAtoms) {

        if (appliesTo.length != 0) {

            Class clazz = appliesTo[0];
            for (Atom a : localAtoms) {
                if (a.getClass().equals(clazz) && !selectedAtoms.contains(a)) {
                    Class[] newAppliesTo = Arrays.copyOfRange(appliesTo, 1, appliesTo.length);
                    List<Atom> newAtoms = new ArrayList<Atom>(localAtoms);
                    List<Atom> newSelectedAtoms = new ArrayList<Atom>(selectedAtoms);
                    newSelectedAtoms.add(a);
                    newAtoms.remove(a);
                    selectAtoms(combinations, newSelectedAtoms, newAppliesTo, newAtoms);
                }

            }

        } else {
            if (!combinations.contains(selectedAtoms)) {
                combinations.add(selectedAtoms);
            }
        }

    }

    /*
     * This method checks for independent operators which basically means:
     *   operatorA.NegativeEffects intersect [ operatorB.precond union operatorB.possitveEffects] = 0 AND
     *   operatorB.NegativeEffects intersect [ operatorA.precond union operatorA.possitveEffects] = 0
     */
    private boolean independent(Operator operatorA, Operator operatorB) {
        List<Predicate> negativeEffectsOperatorA = operatorA.getNegativeEffects();
        List<Predicate> preconditionsOperatorB = operatorB.getPreconditions();
        List<Predicate> possitiveEffectsOperatorB = operatorB.getPossitiveEffects();

        List<Predicate> union = new ArrayList<Predicate>(preconditionsOperatorB.size() + possitiveEffectsOperatorB.size());
        union.addAll(preconditionsOperatorB);
        union.addAll(possitiveEffectsOperatorB);
        for (Predicate p : negativeEffectsOperatorA) {
            if (union.contains(p)) {
                return false;
            }
        }

        return true;

    }

    private Map<Predicate, Set<Predicate>> findMutexedPredicates(List<Operator> groundedOperators,
            Map<Operator, Set<Operator>> mutexedOperators,
            Set<Predicate> nextPredicates) {
        Map<Predicate, Set<Predicate>> mutexPredicates = new HashMap<Predicate, Set<Predicate>>();
        List<Predicate> predicates = new ArrayList<Predicate>();
        for (Predicate p : nextPredicates) {
            if (p instanceof FluentPredicate) {
                predicates.add(p);
            }
        }

        for (int i = 0; i < predicates.size(); i++) {
            for (int j = 0; j < predicates.size() - 1; j++) {
                int count = i + j + 1;
                if (count >= predicates.size()) {
                    count = count - predicates.size();
                }
                Predicate predicateA = predicates.get(i);
                Predicate predicateB = predicates.get(count);

                List<Operator> producesA = new ArrayList<Operator>();
                List<Operator> producesB = new ArrayList<Operator>();
                List<Operator> producesAandB = new ArrayList<Operator>();
                for (Operator o : groundedOperators) {
                    List<Predicate> possitiveEffects = o.getPossitiveEffects();
                    if (possitiveEffects.contains(predicateA)) {
                        producesA.add(o);
                    }
                    if (possitiveEffects.contains(predicateB)) {
                        producesB.add(o);
                    }
                    if (possitiveEffects.contains(predicateA) && possitiveEffects.contains(predicateB)) {
                        producesAandB.add(o);
                    }

                }

                if (!producesA.isEmpty() && !producesB.isEmpty()) {
                    for (int k = 0; k < producesA.size(); k++) {
                        int indepCount = 0;
                        for (Operator oB : producesB) {
                            boolean independentAB = independent(producesA.get(k), oB);
                            if (!independentAB && !predicateA.equals(predicateB)
                                    && mutexedOperators != null
                                    && mutexedOperators.get(producesA.get(k)) != null
                                    && mutexedOperators.get(producesA.get(k)).contains(oB)) {
                                indepCount++;
                            }
                        }

                        if (indepCount == producesB.size()
                                && producesAandB.isEmpty()) {
                            Set<Predicate> p = mutexPredicates.get(predicateA);
                            if (p == null) {
                                mutexPredicates.put(predicateA, new HashSet<Predicate>());
                            }
                            //  System.out.println(">> Adding " + predicateB + " to " + predicateA + " mutexed list via operator independance");
                            mutexPredicates.get(predicateA).add(predicateB);
                        }
                    }

                }

            }

        }

        return mutexPredicates;
    }

    private boolean checkForGroundedCollection(Set<Predicate> predicates) {
        for (Predicate p : predicates) {
            if (!p.isGrounded()) {
                return false;
            }
        }
        return true;
    }

    private boolean isGoalFulfilled(Layer layer) {
        List<Predicate> fluentPredicates = layer.getFluentPredicates();
        List<Predicate> goalPredicates = new ArrayList<Predicate>(goalStatePredicates);
        int goalCounter = 0;

        for (Predicate goalP : goalStatePredicates) {
            if (fluentPredicates.contains(goalP)) {
                goalCounter++;
            }
        }
        
        if (goalCounter == goalStatePredicates.size()) {
            for (int i = 0; i < goalPredicates.size(); i++) {
                for (int j = 0; j < goalPredicates.size() - 1; j++) {
                    int count = i + j + 1;
                    if (count >= goalPredicates.size()) {
                        count = count - goalPredicates.size();
                    }
                    Predicate predicateA = goalPredicates.get(i);
                    Predicate predicateB = goalPredicates.get(count);
                    if (layer.getMutexedPredicates().get(predicateA).contains(predicateB)) {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }

        return true;
    }

    /*
     * Extract from a graph the set of Operators to achieve a goal state
     */
    private Step extract(Graph graph, State goalState, int forLayer) {
       // System.out.println(">> Extract: (layer = " + forLayer + ", goal = [" + goal + "] )");
        if (forLayer == 0) {
            return new StepImpl(new HashSet<Operator>());
        }
        
        // If goal belongs to delta(i) return failure
        if (bannedGoalsForLayer.get(forLayer) != null && bannedGoalsForLayer.get(forLayer).containsAll(goal)) {
            return null;
        }

        Step stepI = gpSearch(graph, goal, new HashSet<Operator>(), forLayer);
        // If StepI != failure return StepI
        if (stepI != null) {
            return stepI;
        }

        // Else add goal to delta(i) and return failure
        if(bannedGoalsForLayer.get(forLayer) == null){
            bannedGoalsForLayer.put(forLayer, new HashSet<Predicate>());
        }
        bannedGoalsForLayer.get(forLayer).addAll(goal);
        return null;

    }

    /*
     * A FIxed Point is found if the last two layers has equal predicates and mutexed predicates
     */
    private boolean reachedFixedPoint(Graph graph) {

        if (graph.size() > 2) {
            Layer lastLayer = graph.getLastLayer();
            Layer previousLayer = graph.getLayer(graph.size() - 2);
            // Quick exit if the number of fluent predicates are not equal
            if (lastLayer.getFluentPredicates().size() != previousLayer.getFluentPredicates().size()) {
                return false;
            } else { // Check for fluent predicates

                for (Predicate p : previousLayer.getFluentPredicates()) {
                    if (!lastLayer.getFluentPredicates().contains(p)) {
                        return false;
                    }
                }
            }
            // Quick exit if the number of mutexed fluent predicates are not equal
            if (lastLayer.getMutexedPredicates().size() != previousLayer.getMutexedPredicates().size()) {
                return false;
            } else { // Check for mutexed fluent predicates
                for (Predicate p : previousLayer.getMutexedPredicates().keySet()) {
                    Set<Predicate> previousLayerMutexPredicates = previousLayer.getMutexedPredicates().get(p);
                    Set<Predicate> lastLayerMutexPredicates = lastLayer.getMutexedPredicates().get(p);
                    for (Predicate pr : previousLayerMutexPredicates) {
                        if (!lastLayerMutexPredicates.contains(pr)) {
                            return false;
                        }
                    }
                }
            }

        } else {
            return false;
        }

        return true;
    }

    

    private Step gpSearch(Graph graph, State goalState, Set<Operator> stepI, int forLayer) {
       // System.out.println("\t >> GPSearch: (layer = " + forLayer + ", goal = [" + goal + "], stepI = [" + stepI + "] )");
        Layer layer = graph.getLayer(forLayer);

        if (goalState.isEmpty()) {
            //  System.out.println(">> GPSearch Goal Empty");
            StateBuilder stateBuilder = new StateBuilder();

            for (Operator o : stepI) {
                for (Predicate p : o.getPreconditions()) {
                    if (p instanceof FluentPredicate) {
                        stateBuilder.addPossitivePredicate(p);
                    }
                }
            }
            path.addStep(forLayer -1, new StepImpl(stepI));
            State newGoalPredicates = stateBuilder.getState();
            return extract(graph, newGoalPredicates, forLayer - 1);
        } else {

            Iterator<Predicate> iterator = goal.iterator();
            Map<Predicate, Set<Operator>> resolversMap = new HashMap<Predicate, Set<Operator>>();
            if (iterator.hasNext()) {
                Predicate next = iterator.next();
                List<Operator> operatorsFromLayer = layer.getOperators();
                for (Operator o : operatorsFromLayer) {
                    if ((o.getPossitiveEffects().contains(next))) {
                        for (Operator op : operatorsFromLayer) {
                            Set<Operator> mutexedOps = layer.getMutexedOperators().get(o);
                            if (resolversMap.get(next) == null) {
                                    resolversMap.put(next, new HashSet<Operator>());
                            }
                            if (mutexedOps != null && !mutexedOps.contains(op)) {
                                resolversMap.get(next).add(o);
                            }else if (mutexedOps == null){
                                resolversMap.get(next).add(o);
                            }
                        }
                    }

                }
                if (resolversMap.get(next) != null && !resolversMap.get(next).isEmpty()) {
                    Predicate predicateWithResolver = resolversMap.keySet().iterator().next();
                    Set<Operator> resolvers = resolversMap.get(predicateWithResolver);
                    if (!resolvers.isEmpty()) {
                        Iterator<Operator> resolversIt = resolvers.iterator();
                        while(resolversIt.hasNext()){
                            Operator operatorA = resolversIt.next();
                            // Find if OperatorA is mutex with any of the Operators in StepI
                            boolean isMutex = false;
                            for(Operator key : stepI){
                                if(layer.getMutexedOperators().get(operatorA) != null && layer.getMutexedOperators().get(key).contains(operatorA)){
                                    isMutex = true;
                                } else if(layer.getMutexedOperators().get(operatorA) != null && layer.getMutexedOperators().get(operatorA).contains(key)){
                                    isMutex = true;
                                }
                            }
                            if(!isMutex){
                            
                                List<Predicate> removePossitiveEffects = operatorA.getPossitiveEffects();
                                Set<Predicate> filteredGoal = new HashSet<Predicate>(goal);


                                for (Predicate p : removePossitiveEffects) {
        
                                    if (filteredGoal.contains(p)) {
                                        filteredGoal.remove(p);
                                    }
                                }
                                Set<Operator> stepIPlusA = new HashSet<Operator>(stepI);
                                stepIPlusA.add(operatorA);
                                return gpSearch(graph, filteredGoal, stepIPlusA, forLayer);
                            }
                        }
                        return null;
                        
                        
                    } else {
                        return null;
                    }

                } else {
                    return null;
                }
            }

        }
        return new StepImpl(stepI);
    }

}
