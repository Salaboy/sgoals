/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.salaboy.sgoals.model.api;

/**
 *
 * @author salaboy
 */
public interface Predicate {
  boolean isNotGrounded();
  boolean isGrounded();
  boolean isPartiallyGrounded();  
  Class[] requiredTypes();
  Atom[] getAtoms();
  String toFormatedString();
}
