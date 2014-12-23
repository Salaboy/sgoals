/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.tests.dinner.operators;

import com.salaboy.sgoals.model.api.Operator;
import com.salaboy.sgoals.model.api.Predicate;
import com.salaboy.sgoals.model.impl.AbstractOperatorImpl;
import com.salaboy.sgoals.tests.dinner.model.Gift;
import com.salaboy.sgoals.tests.dinner.model.Place;
import com.salaboy.sgoals.tests.dinner.predicates.PlaceIsQuiet;
import com.salaboy.sgoals.tests.dinner.predicates.PresentIsReady;

/**
 *
 * @author salaboy
 */
public class WrapOperator extends AbstractOperatorImpl {

    private Gift g;
    private Place p;

    public WrapOperator() {
        super("wrap");
        this.preconditions.add(new PlaceIsQuiet());
        this.possitiveEffects.add(new PresentIsReady());
    }

    public WrapOperator(Gift g, Place p) {
        super("wrap");
        this.g = g;
        this.p = p;
        this.preconditions.add(new PlaceIsQuiet(p));
        this.possitiveEffects.add(new PresentIsReady(g));
    }

    @Override
    public Class[] appliesTo() {
        return new Class[]{Gift.class, Place.class};
    }

    @Override
    public Operator ground(Object... objs) {
        if (objs.length != 2) {
            throw new IllegalStateException("In order to ground this operator you need " + appliesTo());
        }
        return new WrapOperator((Gift) objs[0], (Place) objs[1]);
    }

    @Override
    public boolean isGrounded() {
        return (g != null
                && p != null);
    }

    @Override
    public boolean isPartiallyGrounded() {
        return (g != null
                || p != null);
    }

    @Override
    public boolean isNotGrounded() {
        return (g == null
                && p == null);
    }

    @Override
    public String toFormatedString(boolean full) {
        String result = "Wrap (grounded:" + isGrounded() + "): Gift:" + g
                + " , Place: " + p + "\n";
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
