package com.dudududi.grainexpansion.model.models;


import com.dudududi.grainexpansion.model.cells.Board;
import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.cells.GrainsWarehouse;
import com.dudududi.grainexpansion.model.definables.neighbourhood.NeighbourhoodType;
import javafx.scene.paint.Color;

import java.util.*;
import java.util.stream.Collectors;

public class MonteCarlo {
    private static final int GRAIN_BOUNDARY_ENERGY = 1;

    private Board board;
    private NeighbourhoodType neighbourhoodType;
    private List<Cell.State> cellStates;
    private List<Cell> cellList;
    private Random random;
    private GrainsWarehouse grainsWarehouse;

    public MonteCarlo(Board board, NeighbourhoodType neighbourhoodType, GrainsWarehouse grainsWarehouse) {
        this.board = board;
        this.neighbourhoodType = neighbourhoodType;
        this.cellStates = new ArrayList<>();
        this.cellList = new ArrayList<>(board.getCells());
        this.random = new Random();
        this.grainsWarehouse = grainsWarehouse;
    }

    public void next() {
        Collections.shuffle(cellList);
        for (Cell cell: cellList){
            if (cell.isExcluded()) continue;
            int energyBefore = GRAIN_BOUNDARY_ENERGY * calculateKroneckerDelta(cell, cell.getState());
            Cell.State randomState = selectStateRandomly(cell);
            int energyAfter = GRAIN_BOUNDARY_ENERGY * calculateKroneckerDelta(cell, randomState);

            if (energyAfter - energyBefore <= 0) {
                cell.setState(randomState);
                grainsWarehouse.assign(cell);
            }
        }
    }

    public void initialize(int statesAmount) {
        for (int i = 0; i < statesAmount; i++) {
            cellStates.add(Cell.State.createAliveState());
        }
        board.getCells().forEach(cell -> {
            if (!cell.isExcluded()) {
                cell.setState(cellStates.get(random.nextInt(statesAmount)));
                grainsWarehouse.assign(cell);
            }
        });
    }

    private int calculateKroneckerDelta(Cell origin, Cell.State state) {
        return (int) neighbourhoodType.getNeighbourhood(origin)
                .stream()
                .filter(cell -> !cell.getState().equals(state))
                .count();
    }

    private Cell.State selectStateRandomly(Cell origin) {
        List<Cell> neighbours = neighbourhoodType.getNeighbourhood(origin)
                .stream()
                .filter(cell -> !cell.isExcluded())
                .collect(Collectors.toList());
        return neighbours.get(random.nextInt(neighbours.size() - 1)).getState();
    }

}
