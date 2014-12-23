/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.model.impl.example.predicates;

import com.salaboy.sgoals.model.api.Atom;
import com.salaboy.sgoals.model.impl.AbstractFluentPredicate;
import com.salaboy.sgoals.model.impl.example.model.Robot;
import java.util.Objects;

/**
 *
 * @author salaboy
 */
public class UnloadedPredicateImpl extends AbstractFluentPredicate {

    private Robot r;

    public UnloadedPredicateImpl() {
    }

    public UnloadedPredicateImpl(Robot r) {
        this.r = r;
    }

    @Override
    public Atom[] getAtoms() {
        return new Atom[]{r};
    }
    
    @Override
    public boolean isNotGrounded() {
        return (r == null);
    }

    @Override
    public boolean isGrounded() {
        return (r != null);
    }

    /*
    * When there is a single object the Predicate is Partially Grounded when it is Grounded
    */
    @Override
    public boolean isPartiallyGrounded() {
        return (r != null);
    }

    
    @Override
    public Class[] requiredTypes() {
        return new Class[]{Robot.class};
    }
    
    @Override
    public String toFormatedString() {
        return "Unloaded (grounded: " + isGrounded() + ") Robot: " + r ;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.r);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UnloadedPredicateImpl other = (UnloadedPredicateImpl) obj;
        if (!Objects.equals(this.r, other.r)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "U"+ r;
    }
    
    
    
}
