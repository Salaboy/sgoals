/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.salaboy.sgoals.model.api;


import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author salaboy
 * this represents a Layer Node inside the graphplan graph
 */
public interface Layer {
  String getName();
  Set<Predicate> getPredicates();
  Map<Predicate,Set<Predicate>> getMutexedPredicates();
  List<Predicate> getFluentPredicates();
  List<Predicate> getRigidPredicates();
  List<Operator> getOperators();
  Map<Operator,Set<Operator>> getMutexedOperators();
  Set<Link> getLinks();

}
