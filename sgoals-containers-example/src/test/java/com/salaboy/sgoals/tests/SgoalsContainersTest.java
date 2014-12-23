/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.tests;

import com.salaboy.sgoals.model.api.Operator;
import com.salaboy.sgoals.model.api.Path;
import com.salaboy.sgoals.model.api.Predicate;
import com.salaboy.sgoals.model.api.SGoalsPathFinder;
import com.salaboy.sgoals.model.api.Step;
import com.salaboy.sgoals.core.SGoalsPathFinderImpl;
import com.salaboy.sgoals.model.impl.example.model.Container;
import com.salaboy.sgoals.model.impl.example.model.Location;
import com.salaboy.sgoals.model.impl.example.model.Robot;
import com.salaboy.sgoals.model.impl.example.operators.LoadOperatorImpl;
import com.salaboy.sgoals.model.impl.example.operators.MoveOperatorImpl;
import com.salaboy.sgoals.model.impl.example.operators.UnloadOperatorImpl;
import com.salaboy.sgoals.model.impl.example.predicates.AdjacentPredicateImpl;
import com.salaboy.sgoals.model.impl.example.predicates.AtPredicateImpl;
import com.salaboy.sgoals.model.impl.example.predicates.InPredicateImpl;
import com.salaboy.sgoals.model.impl.example.predicates.UnloadedPredicateImpl;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

/**
 *
 * @author salaboy
 */
public class SgoalsContainersTest {

   

    @Test
    /*
     * example 6.1 page 114
     */
    public void simpleContainersExample() {
        
        
        Location loc1 = new Location("1");
        Location loc2 = new Location("2");

        Container contA = new Container("a");
        Container contB = new Container("b");

        Robot robotR = new Robot("r");
        Robot robotQ = new Robot("q");

  
        Set<Predicate> initialStatePredicates = new HashSet<Predicate>();
        initialStatePredicates.add(new AdjacentPredicateImpl(loc1, loc2)); // This two adjacent were missing in the example graphs
        initialStatePredicates.add(new AdjacentPredicateImpl(loc2, loc1));
        initialStatePredicates.add(new AtPredicateImpl(robotR, loc1)); // r1 // NOTE FOR MYSELF: How is this different from robot.setLocation(loc)
        initialStatePredicates.add(new AtPredicateImpl(robotQ, loc2)); // q2
        initialStatePredicates.add(new InPredicateImpl(contA, loc1));  // a1
        initialStatePredicates.add(new InPredicateImpl(contB, loc2));  // b2
        initialStatePredicates.add(new UnloadedPredicateImpl(robotR)); // ur
        initialStatePredicates.add(new UnloadedPredicateImpl(robotQ)); // uq

        // I will need to Create Operator Definition and Operator Instances
        Set<Operator>  operators = new HashSet<Operator>();
        operators.add(new MoveOperatorImpl());
        operators.add(new LoadOperatorImpl());
        operators.add(new UnloadOperatorImpl());

            
        Set<Predicate> goalStatePredicates = new HashSet<Predicate>();
        goalStatePredicates.add(new InPredicateImpl(contA, loc2));
        goalStatePredicates.add(new InPredicateImpl(contB, loc1));

    // Everything should be goal oriented insted of Plans.. so I should use PathToGoal instead of Plan..
        // and it needs to be something like find paths instead of Planner
        SGoalsPathFinder goalPathFinder = new SGoalsPathFinderImpl(initialStatePredicates, operators, goalStatePredicates);

        Path path = goalPathFinder.process();
        if (path.getSteps().length == 0) {
            System.out.println("There is no solution for this set of initial state, operators and goal state");
        } else {
            for (Step step : path.getSteps()) {
                System.out.println("Step: " + step);
            }
        }

    }
}
