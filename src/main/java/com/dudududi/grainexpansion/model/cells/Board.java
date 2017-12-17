package com.dudududi.grainexpansion.model.cells;

import com.dudududi.grainexpansion.model.definables.Definable;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by dudek on 10/22/17.
 */
public class Board {
    private final Cell[][] cells;

    private final int width;
    private final int height;
    private final boolean isPeriodic;
    private BlockingQueue<Cell.Snapshot> updatesQueue;
    private List<Cell> cellsList;

    public Board(int width, int height, boolean isPeriodic) {
        this.width = width;
        this.height = height;
        this.isPeriodic = isPeriodic;
        cells = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j] = new Cell(new CoordinatePair(i, j));
            }
        }
        cellsList = Arrays.stream(cells).flatMap(Arrays::stream).collect(Collectors.toList());
        updatesQueue = new ArrayBlockingQueue<>(2 * width * height);
    }

    public void resetAll(boolean resetAll) {
        cellsList.forEach( cell -> {
            if (resetAll || cell.isAlive()) {
                cell.reset();
                updatesQueue.add(cell.recordSnapshot());
            }
        });
    }

    public Cell getCell(CoordinatePair position) {
        return cells[position.x][position.y];
    }

    public List<Cell> getCells() {
        return cellsList;
    }

    public BlockingQueue<Cell.Snapshot> getUpdatesQueue() {
        return updatesQueue;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void updateCellState(Cell cell, Cell.State newState) throws InterruptedException {
        cell.setState(newState);
        updatesQueue.put(cell.recordSnapshot());
    }

    public void updateCellEnergy(Cell cell, int newEnergy) {
        cell.setEnergy(newEnergy);
        updatesQueue.add(cell.recordSnapshot());
    }

    public void printToCSVFile(CSVPrinter printer) throws IOException {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                printer.printRecord(cells[i][j].toCSVRecord());
            }
        }
    }

    public static Board restoreFromCSVRecords(CSVParser parser) throws IOException {
        List<CSVRecord> records = parser.getRecords();
        CSVRecord sizeRecord = records.remove(0);
        int width = Integer.parseInt(sizeRecord.get(0));
        int height = Integer.parseInt(sizeRecord.get(1));
        Board board = new Board(width, height, true);
        for (CSVRecord record : records) {
            Cell cell = Cell.fromCSVRecord(record);
            board.cells[cell.getPosition().x][cell.getPosition().y] = cell;
        }
        return board;
    }

    public void printShape(Definable shape, CoordinatePair position, Cell.State state) {
        Cell destination = cells[position.x][position.y];
        shape.defineIndices()
                .stream()
                .map(pos -> calculateAbsolutePosition(pos, destination))
                .filter(Objects::nonNull)
                .map(this::getCell)
                .forEach(cell -> {
                    try {
                        updateCellState(cell, state);
                    } catch (InterruptedException e) {
                        Logger.getGlobal().log(Level.ALL, "Unable to update cell as thread was interrupted: {0}", e);
                    }
                });
    }

    public CoordinatePair calculateAbsolutePosition(CoordinatePair relativePosition, Cell originCell) {
        int i = originCell.getPosition().x + relativePosition.x;
        int j = originCell.getPosition().y + relativePosition.y;
        if (isPeriodic) {
            i = i >= 0 ? i % width : width + (i % width);
            j = j >= 0 ? j % height : height + (j % height);
        } else if (i >= 0 && j >= 0 && i < width && j < height) {
            // neighbourhood is not periodic and indices are out of bound
            return null;
        }
        return new CoordinatePair(i, j);
    }

}
