package com.dudududi.grainexpansion.model.cells;

import com.dudududi.grainexpansion.model.CoordinatePair;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dudek on 4/21/16.
 */
public class Cell {
    private BooleanProperty isAlive;
    private List<Cell> neighbourhood;
    private Random random;
    private CellState state;
    private CoordinatePair position;

    public Cell(CoordinatePair position) {
        random = new Random();
        isAlive = new SimpleBooleanProperty(false);
        state = new CellState(Color.WHITE, CellState.Type.DEAD);
        this.position = position;
    }

    public BooleanProperty getAliveProperty(){
        return isAlive;
    }

    public CoordinatePair getPosition() {
        return position;
    }

    public void setState(CellState state) {
        if(this.state.getType().equals(CellState.Type.ALIVE) && state.getType().equals(CellState.Type.INCLUSION)){
            setAlive(false);
        }
        this.state = state;
        setAlive(!state.getType().equals(CellState.Type.DEAD));
    }

    public Color getColor(){
        return state.getColor();
    }

    public boolean isAlive() {
        return isAlive.get();
    }

    public CellState getState() {
        return state;
    }

    public void setAlive(boolean alive) {
        isAlive.setValue(alive);
    }

    public void setAliveWithRandomColor(){
        Color color = new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1);
        state = new CellState(color, CellState.Type.ALIVE);
        isAlive.setValue(true);
    }

    public List<Cell> getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(List<Cell> neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public List<String> toCSVRecord() {
        List<String> csvRecord = new ArrayList<>();
        csvRecord.add(state.getColor().toString());
        csvRecord.add(String.valueOf(state.getType().getStateId()));
        return csvRecord;
    }
}
