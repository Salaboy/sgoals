/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.model.impl;

import com.salaboy.sgoals.model.api.Path;
import com.salaboy.sgoals.model.api.Step;

/**
 *
 * @author salaboy
 */
public class PathImpl implements Path{
    
    private Step[] steps;

    public PathImpl(int nroOfSteps) {
        this.steps = new Step[nroOfSteps];
    }
    
    public void addStep(int i, Step step){
        this.steps[i] = step;
    }
    
    
    @Override
    public Step[] getSteps() {
        return steps;
    }
    
}
