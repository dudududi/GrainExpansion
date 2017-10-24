package com.dudududi.grainexpansion.model.cells;

import com.dudududi.grainexpansion.model.CoordinatePair;
import com.dudududi.grainexpansion.model.definables.Definable;
import com.dudududi.grainexpansion.model.definables.neighbourhood.NeighbourhoodType;
import javafx.scene.paint.Color;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by dudek on 10/22/17.
 */
public class AutomatonBoard {
    private final Cell[][] cells;
    private final NeighbourhoodType neighbourhoodType;

    private final int width;
    private final int height;

    public AutomatonBoard(int width, int height, NeighbourhoodType neighbourhoodType) {
        this.width = width;
        this.height = height;
        this.neighbourhoodType = neighbourhoodType;
        cells = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                initCell(i, j);
            }
        }
    }

    public void resetAll() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j].reset();
            }
        }
    }

    public Cell getCell(CoordinatePair position) {
        return cells[position.x][position.y];
    }

    private void initCell(int x, int y) {
        Cell cell = cells[x][y];
        if (cells[x][y] == null) {
            cell = cells[x][y] = new Cell(new CoordinatePair(x, y));
        }

        List<Cell> adjacentCells = new ArrayList<>();
        for (CoordinatePair index : neighbourhoodType.defineIndices()) {
            CoordinatePair absolutePos = getAbsolutePosition(index, cell);
            if (absolutePos == null) {
                continue;
            }
            Cell neighbour = getCell(absolutePos);
            if (neighbour == null) {
                // neighbour is not instantiated yet
                neighbour = cells[absolutePos.x][absolutePos.y] = new Cell(absolutePos);
            }
            adjacentCells.add(neighbour);
        }
        cell.setNeighbourhood(adjacentCells);
    }

    public void printToCSVFile(CSVPrinter printer) throws IOException{
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                printer.printRecord(cells[i][j].toCSVRecord());
            }
        }
    }


    public static AutomatonBoard restoreFromCSVRecords(List<CSVRecord> records, int width, int height, NeighbourhoodType type) {
        AutomatonBoard board = new AutomatonBoard(width, height, type);
        for (CSVRecord record : records) {
            int x = Integer.parseInt(record.get("x"));
            int y = Integer.parseInt(record.get("y"));
            Color color = Color.valueOf(record.get("color"));
            Cell.Phase phase = Cell.Phase.byID(Integer.parseInt(record.get("state")));
            Cell cell = new Cell(new CoordinatePair(x, y));
            cell.setState(new Cell.State(phase, color));
            board.cells[x][y] = cell;
        }
        return board;
    }

    public void printShape(Definable shape, CoordinatePair position, Cell.State state) {
        Cell destination = cells[position.x][position.y];
        shape.defineIndices()
                .stream()
                .map(pos -> getAbsolutePosition(pos, destination))
                .filter(Objects::nonNull)
                .map(this::getCell)
                .forEach(cell -> cell.setState(state));
    }

    private CoordinatePair getAbsolutePosition(CoordinatePair relativePosition, Cell originCell) {
        int i = originCell.getPosition().x + relativePosition.x;
        int j = originCell.getPosition().y + relativePosition.y;
        if (neighbourhoodType.isPeriodic()) {
            i = i >= 0 ? i % width : width + (i % width);
            j = j >= 0 ? j % height : height + (j % height);
        } else if (i >= 0 && j >= 0 && i < width && j < height) {
            // neighbourhood is not periodic and indices are out of bound
            return null;
        }
        return new CoordinatePair(i, j);
    }

}
