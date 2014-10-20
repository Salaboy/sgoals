/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.model.impl.example.operators;

import com.salaboy.sgoals.model.api.Operator;
import com.salaboy.sgoals.model.api.Predicate;
import com.salaboy.sgoals.model.impl.AbstractOperatorImpl;

import com.salaboy.sgoals.model.impl.example.predicates.UnloadedPredicateImpl;
import com.salaboy.sgoals.model.impl.example.model.Container;
import com.salaboy.sgoals.model.impl.example.model.Location;
import com.salaboy.sgoals.model.impl.example.model.Robot;
import com.salaboy.sgoals.model.impl.example.predicates.AtPredicateImpl;
import com.salaboy.sgoals.model.impl.example.predicates.InPredicateImpl;
import com.salaboy.sgoals.model.impl.example.predicates.LoadedPredicateImpl;
import java.util.Objects;

/**
 *
 * @author salaboy
 */
public class UnloadOperatorImpl extends AbstractOperatorImpl {

    private Container container;
    private Robot robot;
    private Location location;

    public UnloadOperatorImpl() {
        super("unload");

        this.preconditions.add(new AtPredicateImpl());
        this.preconditions.add(new LoadedPredicateImpl());

        this.negativeEffects.add(new LoadedPredicateImpl());
        this.possitiveEffects.add(new InPredicateImpl());
        this.possitiveEffects.add(new UnloadedPredicateImpl());
    }

    private UnloadOperatorImpl(Container container, Robot robot, Location location) {
        super("unload");

        this.container = container;
        this.robot = robot;
        this.location = location;

        this.preconditions.add(new AtPredicateImpl(robot, location));
        this.preconditions.add(new LoadedPredicateImpl(robot, container));

        this.negativeEffects.add(new LoadedPredicateImpl(robot, container));
        this.possitiveEffects.add(new InPredicateImpl(container, location));
        this.possitiveEffects.add(new UnloadedPredicateImpl(robot));
    }

    @Override
    public boolean isGrounded() {
        return (location != null
                && container != null && robot != null);
    }

    @Override
    public boolean isPartiallyGrounded() {
        return (location != null
                || container != null || robot != null);
    }

    @Override
    public boolean isNotGrounded() {
        return (location == null
                && container == null && robot == null);
    }

    @Override
    public Class[] appliesTo() {
        return new Class[]{Container.class, Robot.class, Location.class};
    }

    @Override
    public Operator ground(Object... objs) {
        if (objs.length != 3) {
            throw new IllegalStateException("In order to ground this operator you need " + appliesTo());
        }
        return new UnloadOperatorImpl((Container) objs[0], (Robot) objs[1], (Location) objs[2]);
    }

    @Override
    public String toFormatedString(boolean full) {
        String result = "Unload (grounded:" + isGrounded() + "): Container:" + container
                + " , Robot: " + robot + " Location: " + location + "\n";
        if (full) {
            result += "\t Effects:";
            result += "\n\t\t preconds: ";
            for (Predicate p : preconditions) {
                result += p.toFormatedString() + ",";
            }
            result += "\n\t\t possitiveEfects: ";
            for (Predicate p : possitiveEffects) {
                result += p.toFormatedString() + ",";
            }
            result += "\n\t\t negativeEfects: ";
            for (Predicate p : negativeEffects) {
                result += p.toFormatedString() + ",";
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return "U" + container + robot + location;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.container);
        hash = 71 * hash + Objects.hashCode(this.robot);
        hash = 71 * hash + Objects.hashCode(this.location);
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
        final UnloadOperatorImpl other = (UnloadOperatorImpl) obj;
        if (!Objects.equals(this.container, other.container)) {
            return false;
        }
        if (!Objects.equals(this.robot, other.robot)) {
            return false;
        }
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        return true;
    }
    
    

}
