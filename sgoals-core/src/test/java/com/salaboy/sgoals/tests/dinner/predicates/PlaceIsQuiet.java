/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.tests.dinner.predicates;

import com.salaboy.sgoals.model.api.Atom;
import com.salaboy.sgoals.model.impl.AbstractFluentPredicate;
import com.salaboy.sgoals.tests.dinner.model.Place;

/**
 *
 * @author salaboy
 */
public class PlaceIsQuiet extends AbstractFluentPredicate {

    private Place pl;

    public PlaceIsQuiet() {
    }

    public PlaceIsQuiet(Place pl) {
        this.pl = pl;
    }

    @Override
    public boolean isNotGrounded() {
        return (pl == null);
    }

    @Override
    public boolean isGrounded() {
        return (pl != null);
    }

    @Override
    public boolean isPartiallyGrounded() {
        return (pl != null);
    }

    @Override
    public Class[] requiredTypes() {
        return new Class[]{Place.class};
    }

    @Override
    public Atom[] getAtoms() {
        return new Atom[]{this.pl};
    }

    @Override
    public String toFormatedString() {
        return "PlaceIsQuite (grounded: " + isGrounded() + ") Place: " + pl;
    }

}
