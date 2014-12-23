/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.salaboy.sgoals.model.impl.example.predicates;

import com.salaboy.sgoals.model.api.Atom;
import com.salaboy.sgoals.model.impl.AbstractRigidPredicate;
import com.salaboy.sgoals.model.impl.example.model.Location;
import java.util.Objects;

/**
 *
 * @author salaboy
 */
public class AdjacentPredicateImpl extends AbstractRigidPredicate{
    private Location loc1;
    private Location loc2;

    public AdjacentPredicateImpl() {
    }

    
    public AdjacentPredicateImpl(Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
    }
    
    @Override
    public Atom[] getAtoms() {
        return new Atom[]{loc1, loc2};
    }

    @Override
    public boolean isNotGrounded() {
        return (loc1 == null && loc2 == null);
    }

    @Override
    public boolean isGrounded() {
        return (loc1 != null && loc2 != null);
    }

    @Override
    public boolean isPartiallyGrounded() {
        return (loc1 != null || loc2 != null);
    }

    @Override
    public Class[] requiredTypes() {
        return new Class[]{Location.class, Location.class};
    }
    
    @Override
    public String toFormatedString() {
        return "Adjacent (grounded: " + isGrounded() + ") Location1: " + loc1 + " Location2: " + loc2;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.loc1);
        hash = 71 * hash + Objects.hashCode(this.loc2);
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
        final AdjacentPredicateImpl other = (AdjacentPredicateImpl) obj;
        if (!Objects.equals(this.loc1, other.loc1)) {
            return false;
        }
        if (!Objects.equals(this.loc2, other.loc2)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AdjacentPredicateImpl{" + "loc1=" + loc1 + ", loc2=" + loc2 + '}';
    }
    
    
    
}
