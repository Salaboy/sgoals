/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.model.impl;

import com.salaboy.sgoals.model.api.Operator;
import com.salaboy.sgoals.model.api.Step;
import java.util.Set;

/**
 *
 * @author salaboy
 */
public class StepImpl implements Step{
    
    private Set<Operator> operators;

    public StepImpl(Set<Operator> operators) {
        this.operators = operators;
    }
    
    @Override
    public Set<Operator> getOperators() {
        return this.operators;
    }

    @Override
    public String toString() {
        return "Step{" + operators + '}';
    }
    
    
    
}
