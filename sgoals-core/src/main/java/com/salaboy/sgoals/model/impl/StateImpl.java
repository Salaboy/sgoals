/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.model.impl;

import com.salaboy.sgoals.model.api.Predicate;
import com.salaboy.sgoals.model.api.State;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author salaboy
 */
public class StateImpl implements State {

    private Set<Predicate> possitvePredicates;
    private Set<Predicate> negativePredicates;

    public StateImpl() {
        this.possitvePredicates = new HashSet<Predicate>();
        this.negativePredicates = new HashSet<Predicate>();
    }

    public void addPossitivePredicate(Predicate p) {
        this.possitvePredicates.add(p);
    }

    public void addNegativePredicate(Predicate p) {
        this.negativePredicates.add(p);
    }

    @Override
    public Set<Predicate> getPossitivePredicates() {
        return possitvePredicates;
    }

    @Override
    public Set<Predicate> getNegativePredicates() {
        return negativePredicates;
    }

    @Override
    public boolean isEmpty() {
        return (possitvePredicates.isEmpty() && negativePredicates.isEmpty());
    }

}
