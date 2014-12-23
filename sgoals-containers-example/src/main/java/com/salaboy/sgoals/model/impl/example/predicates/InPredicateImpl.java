/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.salaboy.sgoals.model.impl.example.predicates;

import com.salaboy.sgoals.model.api.Atom;
import com.salaboy.sgoals.model.impl.AbstractFluentPredicate;
import com.salaboy.sgoals.model.impl.example.model.Container;
import com.salaboy.sgoals.model.impl.example.model.Location;
import java.util.Objects;


/**
 *
 * @author salaboy
 */
public class InPredicateImpl extends AbstractFluentPredicate{
    
    private Container container;
    private Location location;

    public InPredicateImpl() {
    }

    
    public InPredicateImpl(Container container, Location location) {
        this.container = container;
        this.location = location;
    }
    
    @Override
    public Atom[] getAtoms() {
        return new Atom[]{location, container};
    }
    
    @Override
    public boolean isNotGrounded() {
        return (location == null && container == null);
    }

    @Override
    public boolean isGrounded() {
        return (location != null && container != null);
    }

    @Override
    public boolean isPartiallyGrounded() {
        return (location != null || container != null);
    }
    
    @Override
    public Class[] requiredTypes() {
        return new Class[]{Location.class, Container.class};
    }
    
    @Override
    public String toFormatedString() {
        return "In (grounded: " + isGrounded() + ") Location: " + location + " Container: " + container;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.container);
        hash = 83 * hash + Objects.hashCode(this.location);
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
        final InPredicateImpl other = (InPredicateImpl) obj;
        if (!Objects.equals(this.container, other.container)) {
            return false;
        }
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return container + "" + location;
    }
    
    
    
}
