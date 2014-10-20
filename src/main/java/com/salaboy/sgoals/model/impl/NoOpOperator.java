/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.salaboy.sgoals.model.impl;

import com.salaboy.sgoals.model.api.Operator;
import com.salaboy.sgoals.model.api.Predicate;

/**
 *
 * @author salaboy
 */
public class NoOpOperator extends AbstractOperatorImpl{

    private Predicate predicate;
    
    public NoOpOperator(Predicate predicate) {
        super("no-op "+predicate.toFormatedString());
        this.predicate = predicate;
         //Preconditions
        this.preconditions.add(predicate);

        //Effects
        this.possitiveEffects.add(predicate);
        
    }
    

    @Override
    public Class[] appliesTo() {
        return predicate.requiredTypes();
    }

    @Override
    public Operator ground(Object... objs) {
        throw new IllegalStateException("This NO OP Operator is already grounded");
    }

    @Override
    public boolean isGrounded() {
        return true;
    }

    @Override
    public boolean isPartiallyGrounded() {
        return false;
    }

    @Override
    public boolean isNotGrounded() {
        return false;
    }

    @Override
    public String toFormatedString(boolean full) {
        return "No Op Operator for " + predicate.toFormatedString();
    }

    @Override
    public String toString() {
        return "NoOpOperator{" + "predicate=" + predicate + '}';
    }
    
    
}
