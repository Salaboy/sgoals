/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.salaboy.sgoals.model.api;

/**
 *
 * @author salaboy
 */
public interface Graph {
    void addLayer(Layer layer);
    Layer getLastLayer();
    Layer getLayer(int layer);
    void removeLastLayer();
    void removeLayer(int layer);
    int size();
}
