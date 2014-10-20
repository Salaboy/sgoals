/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.model.impl;

import com.salaboy.sgoals.model.api.FluentPredicate;
import com.salaboy.sgoals.model.api.Layer;
import com.salaboy.sgoals.model.api.Link;
import com.salaboy.sgoals.model.api.Operator;
import com.salaboy.sgoals.model.api.Predicate;
import com.salaboy.sgoals.model.api.RigidPredicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author salaboy x
 */
public class LayerImpl implements Layer {

    private String name;
    private Set<Predicate> predicates;
    private Map<Predicate, Set<Predicate>> mutexedPredicates;
    private List<Operator> operators;
    private Map<Operator,Set<Operator>> mutexedOperators;
    private Set<Link> links;

    public LayerImpl(String name, List<Operator> operators, Map<Operator,Set<Operator>> mutexedOperators, 
                                  Set<Predicate> predicates, Map<Predicate, Set<Predicate>> mutexedPredicates, Set<Link> links ) {
        this.name = name;
        this.predicates = predicates;
        this.mutexedPredicates = mutexedPredicates;
        this.operators = operators;
        this.mutexedOperators = mutexedOperators;
        this.links = links;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<Predicate> getPredicates() {
        return predicates;
    }

    @Override
    public List<Operator> getOperators() {
        return operators;
    }

    @Override
    public List<Predicate> getFluentPredicates() {

        List<Predicate> fluentPredicates = new ArrayList<Predicate>();
        for (Predicate p : predicates) {
            if (p instanceof FluentPredicate) {
                fluentPredicates.add(p);
            }
        }
        return fluentPredicates;
    }

    @Override
    public Map<Predicate, Set<Predicate>> getMutexedPredicates() {
        return mutexedPredicates;
    }

    @Override
    public Map<Operator,Set<Operator>> getMutexedOperators() {
        return mutexedOperators;
    }

    @Override
    public List<Predicate> getRigidPredicates() {
        List<Predicate> rigidPredicates = new ArrayList<Predicate>();
        for (Predicate p : predicates) {
            if (p instanceof RigidPredicate) {
                rigidPredicates.add(p);
            }
        }
        return rigidPredicates;
    }

    public Set<Link> getLinks() {
        return links;
    }
 

}
