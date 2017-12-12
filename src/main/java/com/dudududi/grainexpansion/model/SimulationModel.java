package com.dudududi.grainexpansion.model;

import com.dudududi.grainexpansion.model.cells.Board;
import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.cells.CoordinatePair;
import com.dudududi.grainexpansion.model.cells.GrainsWarehouse;
import com.dudududi.grainexpansion.model.definables.Definable;
import com.dudududi.grainexpansion.model.definables.neighbourhood.MooreNeighbourhood;
import com.dudududi.grainexpansion.model.definables.neighbourhood.NeighbourhoodType;
import com.dudududi.grainexpansion.model.models.CellAutomaton;
import com.dudududi.grainexpansion.model.models.MonteCarlo;
import javafx.scene.paint.Color;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimulationModel {
    private CellAutomaton cellAutomaton;
    private MonteCarlo monteCarlo;
    private GrainsWarehouse grainsWarehouse;
    private Board board;
    private List<Observer> observers;

    public SimulationModel(Board board) {
        initialize(board);
        this.observers = new ArrayList<>();
    }

    public MonteCarlo getMonteCarlo() {
        return monteCarlo;
    }

    public Board getBoard() {
        return board;
    }

    public GrainsWarehouse getGrainsWarehouse() {
         return grainsWarehouse;
    }

    public void next(Mode mode) {
        switch (mode) {
            case CELLULAR_AUTOMATON:
                cellAutomaton.next();
                break;
            case MONTE_CARLO:
                monteCarlo.next();
                break;
            default:
                throw new UnsupportedOperationException("Mode " + mode.name() + " is not supported");
        }
    }

    public void attach(Observer observer) {
        if (observer == null) {
            throw new IllegalStateException();
        }
        observers.add(observer);
    }

    public void clear(boolean clearAll) {
        board.resetAll(clearAll);
    }

    public void reinitializeWithBoard(Board board) {
        initialize(board);
        observers.forEach(Observer::onBoardChanged);
    }

    public void handleCellClick(Cell cell) {
        if (cell.isDead()) {
            addNucleon(cell);
        }
        else {
            observers.forEach(observer -> observer.onAliveCellClicked(cell));
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

    public void addNucleon(Cell cell) {
        cell.setState(Cell.State.createAliveState());
        grainsWarehouse.assign(cell);
    }

    public void distributeEnergy(int energy, int boundary) {
        board.getCells().forEach(cell -> cell.setEnergy(energy));
        if (boundary != -1) {
            grainsWarehouse.findBoundary().forEach(cell -> cell.setEnergy(boundary));
        }
    }

    public void reinitializeWihCSV(Reader in) {
        try (CSVParser parser = new CSVParser(in, CSVFormat.DEFAULT.withHeader(Cell.CSV_HEADERS).withSkipHeaderRecord())) {
            Board importedBoard = Board.restoreFromCSVRecords(parser);
            reinitializeWithBoard(importedBoard);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.ALL, "Unable to read CSV file", e);
        }
    }

    public void toCSV(Appendable out) {
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(Cell.CSV_HEADERS))) {
            printer.printRecord(board.getWidth(), board.getHeight());
            board.printToCSVFile(printer);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.ALL, "Unable to create CSV file", e);
        }
    }

    private void initialize(Board board) {
        NeighbourhoodType neighbourhoodType = new MooreNeighbourhood(board);
        this.board = board;
        this.grainsWarehouse = new GrainsWarehouse(board, neighbourhoodType);
        this.cellAutomaton = new CellAutomaton(this.board, neighbourhoodType, grainsWarehouse);
        this.monteCarlo = new MonteCarlo(this.board, neighbourhoodType, grainsWarehouse);
    }

    public enum  Mode {
        CELLULAR_AUTOMATON,
        MONTE_CARLO
    }

}
