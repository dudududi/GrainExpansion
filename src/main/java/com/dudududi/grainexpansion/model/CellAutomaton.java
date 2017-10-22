package com.dudududi.grainexpansion.model;

import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.cells.CellState;
import com.dudududi.grainexpansion.model.neighbourhoods.CellNeighbourhood;
import com.dudududi.grainexpansion.model.rules.Rule;
import javafx.scene.paint.Color;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by dudek on 4/21/16.
 */
public class CellAutomaton {
    private static final String[] CSV_HEADERS = new String[]{"x", "y", "color", "state"};
    private Cell[][] cells;
    private int width, height;
    private List<CoordinatePair> boundaryCells;

    public CellAutomaton(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        boundaryCells = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j] = new Cell(new CoordinatePair(i, j));
                boundaryCells.add(cells[i][j].getPosition());
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
        CellState[][] nextStep = new CellState[width][height];
        boundaryCells = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                nextStep[i][j] = rule.shouldCellBeAlive(cells[i][j]);
                if (rule.isCellOnBoundary(cells[i][j])) {
                    boundaryCells.add(cells[i][j].getPosition());
                }
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (nextStep[i][j] != null){
                    cells[i][j].setState(nextStep[i][j]);
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

    public void printToCSVFile(Appendable out) {
        try(CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(CSV_HEADERS))){
            printer.printRecord(width, height);
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    List<String> cellProps = new ArrayList<>();
                    cellProps.add(String.valueOf(i));
                    cellProps.add(String.valueOf(j));
                    cellProps.addAll(cells[i][j].toCSVRecord());
                    printer.printRecord(cellProps);
                }
            }
        } catch (IOException e){
            Logger.getGlobal().log(Level.ALL, "Unable to create CSV file", e);
        }
    }

    public static CellAutomaton fromCSVFile(Reader in) {
        try(CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withHeader(CSV_HEADERS).withSkipHeaderRecord())){
            List<CSVRecord> records = parser.getRecords();
            CSVRecord sizeRecord = records.remove(0);
            CellAutomaton cellAutomaton = new CellAutomaton(Integer.valueOf(sizeRecord.get(0)), Integer.valueOf(sizeRecord.get(1)));
            for (CSVRecord record: records) {
                int x = Integer.parseInt(record.get("x"));
                int y = Integer.parseInt(record.get("y"));
                Color color = Color.valueOf(record.get("color"));
                CellState.Type type = CellState.Type.byID(Integer.parseInt(record.get("state")));
                cellAutomaton.cells[x][y].setState(new CellState(color, type));
            }
            return cellAutomaton;
        } catch (IOException e){
            Logger.getGlobal().log(Level.ALL, "Unable to read CSV file", e);
        }
        return null;
    }

    public void addInclusions(int amount, int size, boolean isCircural) {
        Random random = new Random();
        for (int i = 0; i < amount; i ++) {
            int positionIndex = random.nextInt(boundaryCells.size());
            CoordinatePair position = boundaryCells.get(positionIndex);
            int x = position.x;
            int y = position.y;
            for (int k = -size/2; k <= size/2; k++) {
                for (int l = -size/2; l <= size/2; l++) {
                    if (x+k < 0 || y + l < 0 || x+k >= width || y+l >= height ||
                            (isCircural && (Math.pow(k, 2) + Math.pow(l,2)) > Math.pow(size/2, 2) )) continue;
                    cells[x+k][y+l].setState(new CellState(Color.BLACK, CellState.Type.INCLUSION));
                }
            }
        }
    }

}
