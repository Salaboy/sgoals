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
 * This represent an action node inside the graphplan layered graph
 */
public interface Operator {
  String getName();  
  List<Predicate> getPossitiveEffects();
  List<Predicate> getNegativeEffects();
  List<Predicate> getPreconditions();
  
  Class[] appliesTo();
  
  
  /*
   * Returns a new grounded operator instance
  */
  Operator ground(Object... objs);
  
  /*
   * checks for grounded operators
  */
  boolean isGrounded();
  boolean isPartiallyGrounded();
  boolean isNotGrounded();
  String toFormatedString(boolean full);
  
}
