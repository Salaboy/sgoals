/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.tests.dinner.predicates;

import com.salaboy.sgoals.model.api.Atom;
import com.salaboy.sgoals.model.impl.AbstractFluentPredicate;
import com.salaboy.sgoals.tests.dinner.model.Gift;

/**
 *
 * @author salaboy
 */
public class PresentIsReady extends AbstractFluentPredicate {

    private Gift g;

    public PresentIsReady() {
    }

    public PresentIsReady(Gift g) {
        this.g = g;
    }

    @Override
    public boolean isNotGrounded() {
        return (g == null);
    }

    @Override
    public boolean isGrounded() {
        return (g != null);
    }

    @Override
    public boolean isPartiallyGrounded() {
        return (g != null);
    }

    @Override
    public Class[] requiredTypes() {
        return new Class[]{Gift.class};
    }

    @Override
    public Atom[] getAtoms() {
        return new Atom[]{this.g};
    }

    @Override
    public String toFormatedString() {
        return "PresentIsReady (grounded: " + isGrounded() + ") Gift: " + g;
    }

}
