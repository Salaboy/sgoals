/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.tests;

import com.salaboy.sgoals.builders.OperatorSetBuilder;
import com.salaboy.sgoals.builders.StateBuilder;
import com.salaboy.sgoals.model.api.Path;
import com.salaboy.sgoals.model.api.SGoalsPathFinder;
import com.salaboy.sgoals.model.impl.PathImpl;
import com.salaboy.sgoals.core.SGoalsPathFinderImpl;
import com.salaboy.sgoals.model.api.Operator;
import com.salaboy.sgoals.model.api.State;
import com.salaboy.sgoals.tests.dinner.model.Dinner;
import com.salaboy.sgoals.tests.dinner.model.Garbage;
import com.salaboy.sgoals.tests.dinner.model.Gift;
import com.salaboy.sgoals.tests.dinner.model.Person;
import com.salaboy.sgoals.tests.dinner.model.Place;
import com.salaboy.sgoals.tests.dinner.operators.CarryOperator;
import com.salaboy.sgoals.tests.dinner.operators.CookOperator;
import com.salaboy.sgoals.tests.dinner.operators.DollyOperator;
import com.salaboy.sgoals.tests.dinner.operators.WrapOperator;
import com.salaboy.sgoals.tests.dinner.predicates.HasCleanHands;
import com.salaboy.sgoals.tests.dinner.predicates.DinnerIsReady;
import com.salaboy.sgoals.tests.dinner.predicates.PlaceHasGarbage;
import com.salaboy.sgoals.tests.dinner.predicates.PresentIsReady;
import com.salaboy.sgoals.tests.dinner.predicates.PlaceIsQuiet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author salaboy
 */
public class SGoalsPathFinderImplTest {
    
    public SGoalsPathFinderImplTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of process method, of class SGoalsPathFinderImpl.
     */
    @Test
    public void testCoreSimple() {
        
        Person p = new Person("salaboy");
        Place pl = new Place("kitchen");
        Garbage g = new Garbage("trash");
        
        
        State initialState = new StateBuilder().addPossitivePredicate(new PlaceHasGarbage(pl, g))
                                                        .addPossitivePredicate(new HasCleanHands(p))
                                                        .addPossitivePredicate(new PlaceIsQuiet(pl))
                                                    .getState(); // Grounded literals, they all should be possitive predicates
        Dinner d = new Dinner("BBQ Ribs");
        Gift gi = new Gift("doll");
        State goalState = new StateBuilder().addPossitivePredicate(new DinnerIsReady(d))
                                            .addPossitivePredicate(new PresentIsReady(gi))
                                            .addNegativePredicate(new PlaceHasGarbage(pl, g)).getState();
        
        Set<Operator> unGroundedOperators = new OperatorSetBuilder().addOperator(new CookOperator())
                                                                    .addOperator(new WrapOperator())
                                                                    .addOperator(new CarryOperator())
                                                                    .addOperator(new DollyOperator())
                                                                    .getOperatorsSet(); 
        
        SGoalsPathFinder instance = new SGoalsPathFinderImpl(initialState, unGroundedOperators, goalState);
        
        Path expResult = new PathImpl(3);
        
        Path result = instance.process();
        
        assertEquals(expResult.getSteps().length, result.getSteps().length);
        
        
    }
    
}
