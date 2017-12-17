package com.dudududi.grainexpansion.model.models;

import com.dudududi.grainexpansion.model.cells.CoordinatePair;
import com.dudududi.grainexpansion.model.cells.Board;
import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.cells.GrainsWarehouse;
import com.dudududi.grainexpansion.model.definables.Definable;
import com.dudududi.grainexpansion.model.definables.neighbourhood.NeighbourhoodType;
import com.dudududi.grainexpansion.model.rules.BasicRule;
import com.dudududi.grainexpansion.model.rules.Rule;
import javafx.scene.paint.Color;
import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * Created by dudek on 4/21/16.
 */
public class CellAutomaton {
    private final Board board;
    private final NeighbourhoodType neighbourhoodType;
    private final GrainsWarehouse grainsWarehouse;
    private final Rule rule;

    public CellAutomaton(Board board, NeighbourhoodType neighbourhoodType, GrainsWarehouse grainsWarehouse) {
        this.board = board;
        this.neighbourhoodType = neighbourhoodType;
        this.grainsWarehouse = grainsWarehouse;
        this.rule = new BasicRule();
    }

    public void next() throws InterruptedException {
        int width = board.getWidth();
        int height = board.getHeight();
        Cell.State[][] nextStep = new Cell.State[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = board.getCell(new CoordinatePair(i, j));
                nextStep[i][j] = rule.determineState(cell, neighbourhoodType);
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (nextStep[i][j] != null) {
                    Cell cell = board.getCell(new CoordinatePair(i, j));
                    board.updateCellState(cell, nextStep[i][j]);
                    grainsWarehouse.assign(cell);
                }
            }
        }
    }

}
