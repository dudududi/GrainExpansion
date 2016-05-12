package com.dudududi.grainexpansion.model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 4/21/16.
 */
public class CellAutomaton {
    private Cell[][] cells;
    private int width, height;
    private CellNeighbourhood neighbourhood;

    public CellAutomaton(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    public void init(CellNeighbourhood neighbourhood) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                List<Cell> adjacentCells = new ArrayList<>();
                for (CoordinatePair index : neighbourhood.getCellNeighbourhood()) {
                    int x = i + index.x;
                    int y = j + index.y;
                    if (neighbourhood.isPeriodic()) {
                        x = x >= 0 ? x % width : width + (x % width );
                        y = y >= 0 ? y % height : height + (y % height );
                        adjacentCells.add(cells[x][y]);
                    } else if (x >= 0 && y >= 0 && x < width && y < height) {
                        adjacentCells.add(cells[x][y]);
                    }
                }
                cells[i][j].setNeighbourhood(adjacentCells);
            }
        }
    }

    public void next(Rule rule) {
        Color[][] nextStep = new Color[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                nextStep[i][j] = rule.shouldCellBeAlive(cells[i][j]);
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (nextStep[i][j] != null){
                    cells[i][j].setColor(nextStep[i][j]);
                    cells[i][j].setAlive(true);
                }
            }
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void clear() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j].setAlive(false);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
