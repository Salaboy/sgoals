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
public class LoadOperatorImpl extends AbstractOperatorImpl {

    private Container container;
    private Robot robot;
    private Location location;

    public LoadOperatorImpl() {
        super("load");
        
        //Preconditions
        this.preconditions.add(new AtPredicateImpl());
        this.preconditions.add(new InPredicateImpl());
        this.preconditions.add(new UnloadedPredicateImpl());

        //Effects
        this.possitiveEffects.add(new LoadedPredicateImpl());
        this.negativeEffects.add(new InPredicateImpl());
        this.negativeEffects.add(new UnloadedPredicateImpl());
    }

    private LoadOperatorImpl(Container container, Robot robot, Location location) {
        super("load");
        this.container = container;
        this.robot = robot;
        this.location = location;
        //Preconditions
        this.preconditions.add(new AtPredicateImpl(robot, location));
        this.preconditions.add(new InPredicateImpl(container, location));
        this.preconditions.add(new UnloadedPredicateImpl(robot));

        //Effects
        this.possitiveEffects.add(new LoadedPredicateImpl(robot, container));
        this.negativeEffects.add(new InPredicateImpl(container, location));
        this.negativeEffects.add(new UnloadedPredicateImpl(robot));
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    @Override
    public boolean isGrounded() {
        return (location != null && container != null && robot != null);
    }

    @Override
    public boolean isPartiallyGrounded() {
        return (location != null || container != null || robot != null);
    }

    @Override
    public boolean isNotGrounded() {
        return (location == null && container == null && robot == null);
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
        return new LoadOperatorImpl((Container) objs[0], (Robot) objs[1], (Location) objs[2]);
    }

    @Override
    public String toFormatedString(boolean full) {
        String result = "Load (grounded:"+isGrounded()+"): Container:" + container 
                + " , Robot: " + robot + " Location: " + location +"\n";
        
        if(full){
            result += "\t Effects: ";
            result += "\n\t\t preconds: ";
            for(Predicate p : preconditions){
                result += p.toFormatedString() + ",";
            }

            result += "\n\t\t possitiveEfects: ";
            for(Predicate p : possitiveEffects){
                result += p.toFormatedString() + ",";
            }
            result += "\n\t\t negativeEfects: ";
            for(Predicate p : negativeEffects){
                result += p.toFormatedString() + ",";
            }
        }
        
        return result;    
    }

    @Override
    public String toString() {
        return "L" + container  + robot  + location;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.container);
        hash = 53 * hash + Objects.hashCode(this.robot);
        hash = 53 * hash + Objects.hashCode(this.location);
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
        final LoadOperatorImpl other = (LoadOperatorImpl) obj;
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
