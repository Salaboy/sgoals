/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.model.impl.example.operators;

import com.salaboy.sgoals.model.api.Operator;
import com.salaboy.sgoals.model.api.Predicate;
import com.salaboy.sgoals.model.impl.AbstractOperatorImpl;
import com.salaboy.sgoals.model.impl.example.predicates.AdjacentPredicateImpl;
import com.salaboy.sgoals.model.impl.example.model.Location;
import com.salaboy.sgoals.model.impl.example.model.Robot;
import com.salaboy.sgoals.model.impl.example.predicates.AtPredicateImpl;
import java.util.Objects;

/**
 *
 * @author salaboy
 */
public class MoveOperatorImpl extends AbstractOperatorImpl {

    private Robot robot;
    private Location location1;
    private Location location2;

    public MoveOperatorImpl() {
        super("move");
        this.preconditions.add(new AdjacentPredicateImpl());
        this.preconditions.add(new AtPredicateImpl());
        this.possitiveEffects.add(new AtPredicateImpl());
        this.negativeEffects.add(new AtPredicateImpl());

    }

    private MoveOperatorImpl(Robot robot, Location location1, Location location2) {
        super("move");
        this.robot = robot;
        this.location1 = location1;
        this.location2 = location2;
        this.preconditions.add(new AdjacentPredicateImpl(location1, location2));
        this.preconditions.add(new AtPredicateImpl(robot, location1));
        this.possitiveEffects.add(new AtPredicateImpl(robot, location2));
        this.negativeEffects.add(new AtPredicateImpl(robot, location1));

    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public Location getLocation1() {
        return location1;
    }

    public void setLocation1(Location location1) {
        this.location1 = location1;
    }

    public Location getLocation2() {
        return location2;
    }

    public void setLocation2(Location location2) {
        this.location2 = location2;
    }

    @Override
    public boolean isGrounded() {
        return (location1 != null && location2 != null && robot != null);
    }

    @Override
    public boolean isPartiallyGrounded() {
        return (location1 != null || location2 != null || robot != null);
    }

    @Override
    public boolean isNotGrounded() {
        return (location1 == null && location2 == null && robot == null);
    }

    @Override
    public Class[] appliesTo() {
        return new Class[]{Robot.class, Location.class, Location.class};
    }

    @Override
    public Operator ground(Object... objs) {
        if (objs.length != 3) {
            throw new IllegalStateException("In order to ground this operator you need " + appliesTo());
        }
        return new MoveOperatorImpl((Robot) objs[0], (Location) objs[1], (Location) objs[2]);
    }

    @Override
    public String toFormatedString(boolean full) {
        String result = "Move (grounded:" + isGrounded() + "): Robot:" + robot
                + " , Location1: " + location1 + " Location2: " + location2 + "\n";
        if (full) {
            result += "\t Effects: ";
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
        return "M" + robot  + location1 + location2;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.robot);
        hash = 79 * hash + Objects.hashCode(this.location1);
        hash = 79 * hash + Objects.hashCode(this.location2);
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
        final MoveOperatorImpl other = (MoveOperatorImpl) obj;
        if (!Objects.equals(this.robot, other.robot)) {
            return false;
        }
        if (!Objects.equals(this.location1, other.location1)) {
            return false;
        }
        if (!Objects.equals(this.location2, other.location2)) {
            return false;
        }
        return true;
    }
    
    

}
