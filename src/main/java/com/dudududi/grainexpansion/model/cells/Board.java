package com.dudududi.grainexpansion.model.cells;

import com.dudududi.grainexpansion.model.definables.Definable;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by dudek on 10/22/17.
 */
public class Board {
    private final Cell[][] cells;

    private final int width;
    private final int height;
    private final boolean isPeriodic;

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
    }

    public void resetAll(boolean resetAll) {
        cellsList.forEach( cell -> {
            if (resetAll || cell.isAlive()) {
                cell.reset();
            }
        });
    }

    public Cell getCell(CoordinatePair position) {
        return cells[position.x][position.y];
    }

    public List<Cell> getCells() {
        return cellsList;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
                .forEach(cell -> cell.setState(state));
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
