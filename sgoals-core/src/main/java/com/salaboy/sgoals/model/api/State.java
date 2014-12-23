/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.model.api;

import java.util.Set;

/**
 *
 * @author salaboy
 */
public interface State {
    Set<Predicate> getPossitivePredicates();
    Set<Predicate> getNegativePredicates();
    void addPossitivePredicate(Predicate p);
    void addNegativePredicate(Predicate p);
    boolean isEmpty();
}
