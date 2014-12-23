/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.builders;

import com.salaboy.sgoals.model.api.Predicate;
import com.salaboy.sgoals.model.api.State;
import com.salaboy.sgoals.model.impl.StateImpl;

/**
 *
 * @author salaboy Simple Fluent State Builder In the future we can add checks
 * here for grounded predicates and non negative predicates for initial states;
 */
public class StateBuilder {

    private State state;

    public StateBuilder() {
        this.state = new StateImpl();
    }

    /*
     * Adds a Possitive Predicate
     */
    public StateBuilder addPossitivePredicate(Predicate predicate) {
        this.state.addPossitivePredicate(predicate);
        return this;
    }

    /*
     * Adds a Negative Predicate
     */
    public StateBuilder addNegativePredicate(Predicate predicate) {
        this.state.addNegativePredicate(predicate);
        return this;
    }

    public State getState() {
        return this.state;
    }
}
