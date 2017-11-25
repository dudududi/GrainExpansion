package com.dudududi.grainexpansion.model;

import com.dudududi.grainexpansion.model.cells.Board;
import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.cells.GrainsWarehouse;
import com.dudududi.grainexpansion.model.definables.Definable;
import com.dudududi.grainexpansion.model.definables.neighbourhood.MooreNeighbourhood;
import com.dudududi.grainexpansion.model.definables.neighbourhood.NeighbourhoodType;
import com.dudududi.grainexpansion.model.rules.Rule;
import javafx.scene.paint.Color;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by dudek on 4/21/16.
 */
public class CellAutomaton {
    private Board board;
    private List<Observer> observers;
    private NeighbourhoodType neighbourhoodType;
    private GrainsWarehouse grainsWarehouse;

    public CellAutomaton(Board board) {
        this.board = board;
        this.observers = new ArrayList<>();
        this.neighbourhoodType = new MooreNeighbourhood(board);
        this.grainsWarehouse = new GrainsWarehouse(board, neighbourhoodType);
    }

    public void next(Rule rule) {
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
                    cell.setState(nextStep[i][j]);
                    grainsWarehouse.assign(cell);
                }
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public void clear() {
        board.resetAll();
    }

    public void printToCSVFile(Appendable out) {
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(Cell.CSV_HEADERS))) {
            printer.printRecord(board.getWidth(), board.getHeight());
            board.printToCSVFile(printer);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.ALL, "Unable to create CSV file", e);
        }
    }

    public static CellAutomaton fromCSVFile(Reader in) {
        try (CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withHeader(Cell.CSV_HEADERS).withSkipHeaderRecord())) {
            List<CSVRecord> records = parser.getRecords();
            CSVRecord sizeRecord = records.remove(0);
            int width = Integer.parseInt(sizeRecord.get(0));
            int height = Integer.parseInt(sizeRecord.get(1));
            Board board = Board.restoreFromCSVRecords(records, width, height);
            return new CellAutomaton(board);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.ALL, "Unable to read CSV file", e);
            return null;
        }
    }

    public void addInclusions(int amount, Definable shape) {
        Random random = new Random();
        List<Cell> boundaryCells = grainsWarehouse.findBoundary();
        for (int i = 0; i < amount; i++) {
            int positionIndex = random.nextInt(boundaryCells.size());
            CoordinatePair position = boundaryCells.get(positionIndex).getPosition();
            board.printShape(shape, position, new Cell.State(Cell.Phase.INCLUSION, Color.BLACK));
        }
    }

    public void reinitializeWithBoard(Board board) {
        this.board = board;
        this.neighbourhoodType = new MooreNeighbourhood(board);
        this.grainsWarehouse = new GrainsWarehouse(board, neighbourhoodType);
        notifyObserversBoardChanged();
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public GrainsWarehouse getGrainsWarehouse() {
        return grainsWarehouse;
    }


    private void notifyObserversBoardChanged() {
        observers.forEach(Observer::onBoardChanged);
    }

}
