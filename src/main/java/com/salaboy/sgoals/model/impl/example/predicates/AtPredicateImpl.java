/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.model.impl.example.predicates;

import com.salaboy.sgoals.model.api.Atom;
import com.salaboy.sgoals.model.impl.AbstractFluentPredicate;
import com.salaboy.sgoals.model.impl.example.model.Location;
import com.salaboy.sgoals.model.impl.example.model.Robot;
import java.util.Objects;

/**
 *
 * @author salaboy
 */
public class AtPredicateImpl extends AbstractFluentPredicate {

    private Robot r;
    private Location loc;

    public AtPredicateImpl() {
    }

    public AtPredicateImpl(Robot r, Location loc) {
        this.r = r;
        this.loc = loc;
    }

    @Override
    public Atom[] getAtoms() {
        return new Atom[]{r, loc};
    }

    @Override
    public boolean isNotGrounded() {
        return (r == null && loc == null);
    }

    @Override
    public boolean isGrounded() {
        return (r != null && loc != null);
    }

    @Override
    public boolean isPartiallyGrounded() {
        return (r != null || loc != null);
    }

    @Override
    public Class[] requiredTypes() {
        return new Class[]{Robot.class, Location.class};
    }

    @Override
    public String toFormatedString() {
        return "At (grounded: " + isGrounded() + ") Robot: " + r + " Location: " + loc;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.r);
        hash = 71 * hash + Objects.hashCode(this.loc);
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
        final AtPredicateImpl other = (AtPredicateImpl) obj;
        if (!Objects.equals(this.r, other.r)) {
            return false;
        }
        if (!Objects.equals(this.loc, other.loc)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  r + "" + loc;
    }
    
    
}
