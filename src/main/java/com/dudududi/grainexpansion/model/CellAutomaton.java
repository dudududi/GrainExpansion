package com.dudududi.grainexpansion.model;

import com.dudududi.grainexpansion.model.cells.AutomatonBoard;
import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.cells.CellState;
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

/**
 * Created by dudek on 4/21/16.
 */
public class CellAutomaton {
    private AutomatonBoard board;
    private int width, height;
    private List<CoordinatePair> boundaryIndices;

    public CellAutomaton(NeighbourhoodType neighbourhoodType, int width, int height) {
        this(new AutomatonBoard(width, height, neighbourhoodType));
    }

    private CellAutomaton(AutomatonBoard board){
        this.board = board;
    }

    public void next(Rule rule) {
        Cell.State[][] nextStep = new Cell.State[width][height];
        boundaryIndices = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = board.getCell(new CoordinatePair(i, j));
                nextStep[i][j] = rule.shouldCellBeAlive(cell);
                if (rule.isCellOnBoundary(cell)) {
                    boundaryIndices.add(cell.getPosition());
                }
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                board.getCell(new CoordinatePair(i, j)).setState(nextStep[i][j]);
            }
        }
    }

    public void clear() {
        board.resetAll();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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
            AutomatonBoard board = AutomatonBoard.restoreFromCSVRecords(records, width, height, new MooreNeighbourhood(true));
            return new CellAutomaton(board);
        } catch (IOException e){
            Logger.getGlobal().log(Level.ALL, "Unable to read CSV file", e);
            return null;
        }
    }

    public void addInclusions(int amount, Definable shape) {
        Random random = new Random();
        for (int i = 0; i < amount; i ++) {
            int positionIndex = random.nextInt(boundaryIndices.size());
            CoordinatePair position = boundaryIndices.get(positionIndex);
            board.printShape(shape, position, new Cell.State(Cell.Phase.INCLUSION, Color.BLACK));
        }
    }

    public void addOnChangeListener(){

    }

}
