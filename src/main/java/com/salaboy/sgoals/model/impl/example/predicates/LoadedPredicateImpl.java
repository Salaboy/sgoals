/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.salaboy.sgoals.model.impl.example.predicates;

import com.salaboy.sgoals.model.api.Atom;
import com.salaboy.sgoals.model.impl.AbstractFluentPredicate;
import com.salaboy.sgoals.model.impl.example.model.Container;
import com.salaboy.sgoals.model.impl.example.model.Robot;
import java.util.Objects;

/**
 *
 * @author salaboy
 */
public class LoadedPredicateImpl extends AbstractFluentPredicate{
    private Robot r;
    private Container container;

    public LoadedPredicateImpl() {
    }

    
    public LoadedPredicateImpl(Robot r, Container container) {
        this.r = r;
        this.container = container;
    }
    
    @Override
    public Atom[] getAtoms() {
        return new Atom[]{r, container};
    }
    
    @Override
    public boolean isNotGrounded() {
        return (r == null && container == null);
    }

    @Override
    public boolean isGrounded() {
        return (r != null && container != null);
    }

    @Override
    public boolean isPartiallyGrounded() {
        return (r != null || container != null);
    }
    
    @Override
    public Class[] requiredTypes() {
        return new Class[]{Robot.class, Container.class};
    }
    
    @Override
    public String toFormatedString() {
        return "Loaded (grounded: " + isGrounded() + ") Robot: " + r + " Container: " + container;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.r);
        hash = 79 * hash + Objects.hashCode(this.container);
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
        final LoadedPredicateImpl other = (LoadedPredicateImpl) obj;
        if (!Objects.equals(this.r, other.r)) {
            return false;
        }
        if (!Objects.equals(this.container, other.container)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return container + "" + r;
    }
    
    
    
}
