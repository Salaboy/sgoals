/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.model.impl;

import com.salaboy.sgoals.model.api.Operator;
import java.util.Objects;

/**
 *
 * @author salaboy
 */
public class OperatorsPair {
    private Operator o1;
    private Operator o2;

    public OperatorsPair(Operator o1, Operator o2) {
        this.o1 = o1;
        this.o2 = o2;
    }

    public Operator getO1() {
        return o1;
    }

    public Operator getO2() {
        return o2;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.o1);
        hash = 17 * hash + Objects.hashCode(this.o2);
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
        final OperatorsPair other = (OperatorsPair) obj;
        if (!Objects.equals(this.o1, other.o1)) {
            return false;
        }
        if (!Objects.equals(this.o2, other.o2)) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString() {
        return "OperatorsPair{" +  o1 + " , " + o2 + '}';
    }
    
    
}
