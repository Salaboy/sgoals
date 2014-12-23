/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.salaboy.sgoals.model.api;

import java.util.List;

/**
 *
 * @author salaboy
 */
public interface GroundedState {
    List<Predicate> getPredicates();
    void addPredicate(Predicate p);
}
