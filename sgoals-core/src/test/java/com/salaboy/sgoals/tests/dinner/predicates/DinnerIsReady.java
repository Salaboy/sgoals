/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.tests.dinner.predicates;

import com.salaboy.sgoals.model.api.Atom;
import com.salaboy.sgoals.model.impl.AbstractFluentPredicate;
import com.salaboy.sgoals.tests.dinner.model.Dinner;

/**
 *
 * @author salaboy
 */
public class DinnerIsReady extends AbstractFluentPredicate{

    private Dinner d;

    public DinnerIsReady() {
    
    }

    public DinnerIsReady(Dinner d) {
        this.d = d;
    }

    @Override
    public boolean isNotGrounded() {
        return (d == null);
    }

    @Override
    public boolean isGrounded() {
        return (d != null);
    }

    @Override
    public boolean isPartiallyGrounded() {
        return (d != null);
    }

    @Override
    public Class[] requiredTypes() {
        return new Class[]{Dinner.class};
    }

    @Override
    public Atom[] getAtoms() {
        return new Atom[]{this.d};
    }

    @Override
    public String toFormatedString() {
        return "DinnerIsReady (grounded: " + isGrounded() + ") Dinner: " + d;
    }
    
    
    
    
    
}
