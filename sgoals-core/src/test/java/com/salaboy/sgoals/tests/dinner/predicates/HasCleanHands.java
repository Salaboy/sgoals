/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.tests.dinner.predicates;

import com.salaboy.sgoals.model.api.Atom;
import com.salaboy.sgoals.model.impl.AbstractFluentPredicate;
import com.salaboy.sgoals.tests.dinner.model.Person;

/**
 *
 * @author salaboy
 */
public class HasCleanHands extends AbstractFluentPredicate {

    private Person p;

    public HasCleanHands() {
    }

    public HasCleanHands(Person p) {
        this.p = p;
    }

    @Override
    public boolean isNotGrounded() {
        return (p == null);
    }

    @Override
    public boolean isGrounded() {
        return (p != null);
    }

    @Override
    public boolean isPartiallyGrounded() {
        return (p != null);
    }

    @Override
    public Class[] requiredTypes() {
        return new Class[]{Person.class};
    }

    @Override
    public Atom[] getAtoms() {
        return new Atom[]{p};
    }

    @Override
    public String toFormatedString() {
        return "HasCleanHands (grounded: " + isGrounded() + ") Person: " + p;
    }

}
