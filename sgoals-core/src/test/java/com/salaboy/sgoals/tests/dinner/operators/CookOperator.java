/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.tests.dinner.operators;

import com.salaboy.sgoals.model.api.Operator;
import com.salaboy.sgoals.model.api.Predicate;
import com.salaboy.sgoals.model.impl.AbstractOperatorImpl;
import com.salaboy.sgoals.tests.dinner.model.Dinner;
import com.salaboy.sgoals.tests.dinner.model.Person;
import com.salaboy.sgoals.tests.dinner.predicates.HasCleanHands;
import com.salaboy.sgoals.tests.dinner.predicates.DinnerIsReady;

/**
 *
 * @author salaboy
 */
public class CookOperator extends AbstractOperatorImpl {
    
    private Person p;
    private Dinner d;

    public CookOperator() {
        super("cook");
        this.preconditions.add(new HasCleanHands());
        this.possitiveEffects.add(new DinnerIsReady());
    }
    
    public CookOperator(Person p, Dinner d) {
        super("cook");
        this.p = p;
        this.d = d;
        this.preconditions.add(new HasCleanHands(p));
        this.possitiveEffects.add(new DinnerIsReady(d));
    }

    @Override
    public Class[] appliesTo() {
        return new Class[]{Person.class};
    }

    @Override
    public Operator ground(Object... objs) {
         if (objs.length != 2) {
            throw new IllegalStateException("In order to ground this operator you need " + appliesTo());
        }
        return new CookOperator((Person) objs[0], (Dinner) objs[1]);
    }

    @Override
    public boolean isGrounded() {
        return (p != null && d != null);
    }

    @Override
    public boolean isPartiallyGrounded() {
        return (p != null || d != null);
    }

    @Override
    public boolean isNotGrounded() {
        return (p == null && d == null);
    }

    @Override
    public String toFormatedString(boolean full) {
        String result = "Cook (grounded:" + isGrounded() + "): Person:" + p + " Dinner: "+d+" \n";
        if (full) {
            result += "\t Effects:";
            result += "\n\t\t preconds: ";
            for (Predicate p : preconditions) {
                result += p.toFormatedString() + ",";
            }
            result += "\n\t\t possitiveEfects: ";
            for (Predicate p : possitiveEffects) {
                result += p.toFormatedString() + ",";
            }
            result += "\n\t\t negativeEfects: ";
            for (Predicate p : negativeEffects) {
                result += p.toFormatedString() + ",";
            }
        }

        return result;
    }

}
