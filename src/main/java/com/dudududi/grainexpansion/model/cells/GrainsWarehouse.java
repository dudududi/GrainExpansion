package com.dudududi.grainexpansion.model.cells;


import com.dudududi.grainexpansion.model.definables.neighbourhood.NeighbourhoodType;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GrainsWarehouse {

    private Map<Cell.State, List<Cell>> grains;
    private Board board;
    private NeighbourhoodType neighbourhoodType;

    public GrainsWarehouse(Board board, NeighbourhoodType neighbourhoodType) {
        this.board = board;
        this.grains = new HashMap<>();
        this.neighbourhoodType = neighbourhoodType;
    }

    public List<Cell> getByCell(Cell cell) {
        Cell.State state = cell.getState();
        if (!grains.containsKey(state)) {
            grains.put(state, new ArrayList<>());
        }
        return grains.get(state);
    }

    public void assign(Cell cell) {
        List<Cell> grainCells = getByCell(cell);
        grainCells.add(cell);
    }

    public void changeGrainState(Cell origin, Cell.State newState) {
        List<Cell> grainCells = grains.remove(origin.getState());
        grainCells.forEach(cell -> {
            try {
                board.updateCellState(cell, newState);
            } catch (InterruptedException e) {
                Logger.getGlobal().log(Level.ALL, "Unable to update grain state {0}", e);
            }
        });

        // duplicates should not be overwritten (look at the implementation of Cell.State#hashCode method)
        grains.put(newState, grainCells);
    }

    public void changeGrainBoundariesState(Cell origin, int size) {
        findBoundary(origin).forEach(cell -> drawBoundaries(cell, size));
    }

    public void changeAllBoundariesState(int size) {
        findBoundary().forEach(cell -> drawBoundaries(cell, size));
    }

    public List<Cell> findBoundary() {
        return board.getCells()
                .stream()
                .filter(this::isCellOnBoundary)
                .collect(Collectors.toList());
    }

    private List<Cell> findBoundary(Cell origin) {
        Cell.State originState = origin.getState();
        return grains.get(originState).stream()
                .filter(this::isCellOnBoundary)
                .collect(Collectors.toList());
    }

    private boolean isCellOnBoundary(Cell cell) {
        return neighbourhoodType.getNeighbourhood(cell)
                .stream()
                .anyMatch(c -> !(c.getState().equals(cell.getState())));
    }

    private void drawBoundaries(Cell origin, int size) {
        try {
            for (int i = -size / 2; i <= size / 2; i++) {
                for (int j = -size / 2; j <= size / 2; j++) {
                    CoordinatePair absolutePosition = board.calculateAbsolutePosition(new CoordinatePair(i, j), origin);
                    if (absolutePosition != null) {
                        board.updateCellState(board.getCell(absolutePosition), new Cell.State(Cell.Phase.BOUNDARY, Color.BLACK));
                    }
                }

            }
        } catch (InterruptedException e) {
            Logger.getGlobal().log(Level.ALL, "Error during processing boundaries: {0}", e);
        }
    }

}
