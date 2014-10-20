/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.model.impl;

import com.salaboy.sgoals.model.api.Atom;
import com.salaboy.sgoals.model.api.FluentPredicate;
import com.salaboy.sgoals.model.api.Layer;
import com.salaboy.sgoals.model.api.Link;
import com.salaboy.sgoals.model.api.Operator;
import com.salaboy.sgoals.model.api.Path;
import com.salaboy.sgoals.model.api.Predicate;
import com.salaboy.sgoals.model.api.SGoalsPathFinder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author salaboy
 */
public class SGoalsPathFinderImpl implements SGoalsPathFinder {

    private Layer initialLayer;
    private Set<Predicate> initialStatePredicates;
    private Set<Predicate> goalStatePredicates;
    private List<Operator> unGroundedOperators;
    private int layerCount = 0;

    public SGoalsPathFinderImpl(Set<Predicate> initialStatePredicates, List<Operator> operators, Set<Predicate> goalStatePredicates) {
        if (!checkForGroundedCollection(initialStatePredicates)) {
            throw new IllegalStateException("The initial state predicates must be grounded");
        }
        if (!checkForGroundedCollection(goalStatePredicates)) {
            throw new IllegalStateException("The goal state predicates must be grounded");
        }
        this.unGroundedOperators = operators;
        this.initialStatePredicates = initialStatePredicates;
        this.goalStatePredicates = goalStatePredicates;
        initialLayer = new LayerImpl("initialLayer", null, null, initialStatePredicates, null, null);

    }

    @Override
    public Path[] process() {

        // First I need to check if the operator applies to the initial state
        // then I need to verify that when it is grounded is applicable
        // then I need to check for the intersection with the mutex collection for the state
        expand(initialLayer);

        return new Path[0];
    }
    /*
     *   This method is supposed to take all the current layer and calculate the next one 
     *   iteratively
     */

    private void expand(Layer layer) {
        System.out.println("-----------------------------------------");
        System.out.println("Layer : " + layer.getName());
        if (isGoalFulfilled(layer)) {
            return;
        }

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
        Layer newLayer = new LayerImpl("layer" + layerCount, groundedOperators, mutexedOperators, nextPredicates, mutexedPredicates, links);
        expand(newLayer);
        processGoal(newLayer);
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
//                System.out.println(" Predicate A: " + predicateA + " is produced by:");
//                for (Operator op : producesA) {
//                    System.out.println("\t\t\t Op: " + op);
//                }
//
//                System.out.println(" Predicate B: " + predicateB + " is produced by:");
//                for (Operator op : producesB) {
//                    System.out.println("\t\t\t Op: " + op);
//                }

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
        System.out.println("Goal Target = " + goalStatePredicates.size());
        System.out.println("Goal Counter = " + goalCounter);
        if (goalCounter == goalStatePredicates.size()) {
            for (int i = 0; i < goalPredicates.size(); i++) {
                for (int j = 0; j < goalPredicates.size() - 1; j++) {
                    int count = i + j + 1;
                    if (count >= goalPredicates.size()) {
                        count = count - goalPredicates.size();
                    }
                    Predicate predicateA = goalPredicates.get(i);
                    Predicate predicateB = goalPredicates.get(count);
                    if(layer.getMutexedPredicates().get(predicateA).contains(predicateB)){
                        return false;
                    }
                }
            }
        } else {
            return false;
        }

        return true;
    }

    private void processGoal(Layer layer) {
        System.out.println(">> Processing Layer : "+layer.getName());
        Set<Link> links = layer.getLinks();
        for(Link l : links){
            // The first time the goals to check are goalStatePredicates, but then the next layer have different goals
            // which are the pre conditions of the actions selected in the previous layer
            so do as the comment says!
            if(l.getType().equals("possitive") && goalStatePredicates.contains(l.getPredicate())){
                System.out.println("Selected Link: "+l);
            }
        }
    }

}
