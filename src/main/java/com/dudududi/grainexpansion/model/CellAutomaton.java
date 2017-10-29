package com.dudududi.grainexpansion.model;

import com.dudududi.grainexpansion.model.cells.Board;
import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.definables.Definable;
import com.dudududi.grainexpansion.model.definables.neighbourhood.MooreNeighbourhood;
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
    private int width, height;

    public CellAutomaton(Board board){
        this.board = board;
        this.width = board.getWidth();
        this.height = board.getHeight();
    }

    public void next(Rule rule) {
        Cell.State[][] nextStep = new Cell.State[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = board.getCell(new CoordinatePair(i, j));
                nextStep[i][j] = rule.determineState(cell);
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (nextStep[i][j] != null) {
                    board.getCell(new CoordinatePair(i, j)).setState(nextStep[i][j]);
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
        try(CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(Cell.CSV_HEADERS))){
            printer.printRecord(width, height);
            board.printToCSVFile(printer);
        } catch (IOException e){
            Logger.getGlobal().log(Level.ALL, "Unable to create CSV file", e);
        }
    }

    public static CellAutomaton fromCSVFile(Reader in) {
        try(CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withHeader(Cell.CSV_HEADERS).withSkipHeaderRecord())){
            List<CSVRecord> records = parser.getRecords();
            CSVRecord sizeRecord = records.remove(0);
            int width = Integer.parseInt(sizeRecord.get(0));
            int height = Integer.parseInt(sizeRecord.get(1));
            Board board = Board.restoreFromCSVRecords(records, width, height);
            return new CellAutomaton(board);
        } catch (IOException e){
            Logger.getGlobal().log(Level.ALL, "Unable to read CSV file", e);
            return null;
        }
    }

    public void addInclusions(int amount, Definable shape) {
        Random random = new Random();
        List<Cell> boundaryCells = findBoundaryCells();
        for (int i = 0; i < amount; i ++) {
            int positionIndex = random.nextInt(boundaryCells.size());
            CoordinatePair position = boundaryCells.get(positionIndex).getPosition();
            board.printShape(shape, position, new Cell.State(Cell.Phase.INCLUSION, Color.BLACK));
        }
    }

    private List<Cell> findBoundaryCells() {
        return board.getCells()
                .stream()
                .filter(this::isCellOnBoundary)
                .collect(Collectors.toList());
    }

    private boolean isCellOnBoundary(Cell cell) {
        MooreNeighbourhood neighbourhood = new MooreNeighbourhood(board);
        return neighbourhood.getNeighbourhood(cell)
                .anyMatch(c -> !(c.getState().equals(cell.getState()) && cell.isAlive()));
    }
}
