/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.model.impl;

import com.salaboy.sgoals.model.api.Operator;
import com.salaboy.sgoals.model.api.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author salaboy
 */
public abstract class AbstractOperatorImpl implements Operator {

    protected String name;
    protected List<Predicate> preconditions;

    protected List<Predicate> possitiveEffects;
    protected List<Predicate> negativeEffects;

    public AbstractOperatorImpl(String name) {
        this.name = name;

        this.preconditions = new ArrayList<Predicate>();
        this.possitiveEffects = new ArrayList<Predicate>();
        this.negativeEffects = new ArrayList<Predicate>();

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Predicate> getPossitiveEffects() {
        return possitiveEffects;
    }

    @Override
    public List<Predicate> getNegativeEffects() {
        return negativeEffects;
    }

    @Override
    public List<Predicate> getPreconditions() {
        return preconditions;
    }

}
