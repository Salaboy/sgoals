/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.tests;

import com.salaboy.sgoals.model.api.Operator;
import com.salaboy.sgoals.model.impl.example.operators.MoveOperatorImpl;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
public class GroundingTest {

    public GroundingTest() {
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

    @Test
    public void simpleGrounding() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException {
        MoveOperatorImpl moveOperatorImpl = new MoveOperatorImpl();
        assertTrue(moveOperatorImpl.isNotGrounded());
        assertTrue(!moveOperatorImpl.isGrounded());
        Class[] appliesTo = moveOperatorImpl.appliesTo();
        Object[] objs = new Object[appliesTo.length];
        int count = 0;
        for(Class clazz : appliesTo){
            Constructor<?> constructor = Class.forName(clazz.getCanonicalName()).getConstructor(String.class);
            objs[count] = constructor.newInstance(clazz.getCanonicalName()+count);
            count++;
        }
        MoveOperatorImpl groundedMoveOperator = (MoveOperatorImpl)moveOperatorImpl.ground(objs);
        assertTrue(!groundedMoveOperator.isNotGrounded());
        assertTrue(groundedMoveOperator.isGrounded());
    }
    
    @Test
    public void multiGrounding(){
        
    }
}
