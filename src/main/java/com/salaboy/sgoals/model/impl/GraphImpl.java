/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.model.impl;

import com.salaboy.sgoals.model.api.Graph;
import com.salaboy.sgoals.model.api.Layer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author salaboy
 */
public class GraphImpl implements Graph {
    private List<Layer> layers = new ArrayList<Layer>();

    public GraphImpl() {
    }
    
    public void addLayer(Layer layer){
        layers.add(layer);
    }
    
    public Layer getLastLayer(){
        return layers.get(layers.size()-1);
    }
    
    
    
    public int size(){
        return layers.size();
    }

    @Override
    public Layer getLayer(int layer) {
        return layers.get(layer);
    }

    @Override
    public void removeLastLayer() {
        layers.remove(layers.size()-1);
    }

    @Override
    public void removeLayer(int layer) {
        layers.remove(layer);
    }
    
}
