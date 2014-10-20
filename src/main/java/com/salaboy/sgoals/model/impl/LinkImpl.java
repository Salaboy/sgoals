/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.model.impl;

import com.salaboy.sgoals.model.api.Link;
import com.salaboy.sgoals.model.api.Operator;
import com.salaboy.sgoals.model.api.Predicate;
import java.util.Objects;

/**
 *
 * @author salaboy
 */
public class LinkImpl implements Link {

    private Operator operator;
    private Predicate predicate;
    private String type;

    public LinkImpl(Operator operator, Predicate predicate, String type) {
        this.operator = operator;
        this.predicate = predicate;
        this.type = type;
    }

    @Override
    public Operator getOperator() {
        return operator;
    }

    @Override
    public Predicate getPredicate() {
        return predicate;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.operator);
        hash = 37 * hash + Objects.hashCode(this.predicate);
        hash = 37 * hash + Objects.hashCode(this.type);
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
        final LinkImpl other = (LinkImpl) obj;
        if (!Objects.equals(this.operator, other.operator)) {
            return false;
        }
        if (!Objects.equals(this.predicate, other.predicate)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "LinkImpl{" + "operator=" + operator + ", predicate=" + predicate + ", type=" + type + '}';
    }

    
 
    

}
