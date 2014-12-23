/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.tests.dinner.operators;

import com.salaboy.sgoals.model.api.Operator;
import com.salaboy.sgoals.model.api.Predicate;
import com.salaboy.sgoals.model.impl.AbstractOperatorImpl;
import com.salaboy.sgoals.tests.dinner.model.Garbage;
import com.salaboy.sgoals.tests.dinner.model.Person;
import com.salaboy.sgoals.tests.dinner.model.Place;
import com.salaboy.sgoals.tests.dinner.predicates.HasCleanHands;
import com.salaboy.sgoals.tests.dinner.predicates.PlaceHasGarbage;

/**
 *
 * @author salaboy
 */
public class CarryOperator extends AbstractOperatorImpl {

    private Garbage g;
    private Person p;
    private Place pl;

    public CarryOperator() {
        super("carry");
        this.preconditions.add(new PlaceHasGarbage());
        this.negativeEffects.add(new PlaceHasGarbage());
        this.negativeEffects.add(new HasCleanHands());
    }

    public CarryOperator(Garbage g, Person p, Place pl) {
        super("carry");
        this.g = g;
        this.p = p;
        this.pl = pl;
        this.preconditions.add(new PlaceHasGarbage(pl, g));
        this.negativeEffects.add(new PlaceHasGarbage(pl, g));
        this.negativeEffects.add(new HasCleanHands(p));
    }

    @Override
    public String getName() {
        return "carry";
    }

    @Override
    public Class[] appliesTo() {
        return new Class[]{Garbage.class, Person.class, Place.class};
    }

    @Override
    public Operator ground(Object... objs) {
        if (objs.length != 3) {
            throw new IllegalStateException("In order to ground this operator you need " + appliesTo());
        }
        return new CarryOperator((Garbage) objs[0], (Person) objs[1], (Place) objs[2]);
    }

    @Override
    public boolean isGrounded() {
        return (g != null && p != null && pl != null);
    }

    @Override
    public boolean isPartiallyGrounded() {
        return (g != null || p != null || pl != null);
    }

    @Override
    public boolean isNotGrounded() {
        return (g == null && p == null && pl == null);
    }

    @Override
    public String toFormatedString(boolean full) {
        String result = "Carry (grounded:" + isGrounded() + "): Garbage:" + g + " Person: " + p + " Place: " + pl + " \n";
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
