package com.dudududi.grainexpansion.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Random;

/**
 * Created by dudek on 4/21/16.
 */
public class Cell {
    private BooleanProperty isAlive;
    private List<Cell> neighbourhood;
    private Color color;
    private Random random;

    public Cell() {
        random = new Random();
        isAlive = new SimpleBooleanProperty(false);
        color = Color.WHITE;
    }

    public BooleanProperty getAliveProperty(){
        return isAlive;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    public boolean isAlive() {
        return isAlive.get();
    }

    public void setAlive(boolean alive) {
        isAlive.setValue(alive);
    }

    public void setAliveWithRandomColor(){
        color = new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1);
        isAlive.setValue(true);
    }

    public List<Cell> getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(List<Cell> neighbourhood) {
        this.neighbourhood = neighbourhood;
    }
}
