/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.builders;

import com.salaboy.sgoals.model.api.Operator;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author salaboy
 */
public class OperatorSetBuilder {
    private Set<Operator> operators;
    private boolean grounded = false;
    public OperatorSetBuilder() {
        this.operators = new HashSet<Operator>();
    }
    
    public OperatorSetBuilder(boolean grounded) {
        this.operators = new HashSet<Operator>();
    }

    public OperatorSetBuilder addOperator(Operator operator) {
        this.operators.add(operator);
        return this;
    }

    public Set<Operator> getOperatorsSet() {
        return this.operators;
    }
}
