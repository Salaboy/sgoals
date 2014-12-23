/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.tests.dinner.predicates;

import com.salaboy.sgoals.model.api.Atom;
import com.salaboy.sgoals.model.api.FluentPredicate;
import com.salaboy.sgoals.tests.dinner.model.Garbage;
import com.salaboy.sgoals.tests.dinner.model.Place;

/**
 *
 * @author salaboy
 */
public class PlaceHasGarbage implements FluentPredicate {

    private Place pl;
    private Garbage g;

    public PlaceHasGarbage() {
    }

    public PlaceHasGarbage(Place pl, Garbage g) {
        this.pl = pl;
        this.g = g;
    }

    @Override
    public boolean isNotGrounded() {
        return (pl == null && g == null);
    }

    @Override
    public boolean isGrounded() {
        return (pl != null && g != null);
    }

    @Override
    public boolean isPartiallyGrounded() {
        return (pl != null || g != null);
    }

    @Override
    public Class[] requiredTypes() {
        return new Class[]{Place.class, Garbage.class};
    }

    @Override
    public Atom[] getAtoms() {
        return new Atom[]{pl, g};
    }

    @Override
    public String toFormatedString() {
        return "PlaceHasGarbage (grounded: " + isGrounded() + ") Place: " + pl + " Garbage: " + g;
    }

}
