package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.CellAutomaton;
import com.dudududi.grainexpansion.model.CoordinatePair;
import com.dudududi.grainexpansion.model.cells.Board;
import com.dudududi.grainexpansion.model.cells.Cell;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.Random;

public class NucleonsController implements Controller {
    private static final int DEFAULT_NUCLEONS_AMOUNT = 25;

    private Random random;
    private CellAutomaton cellAutomaton;

    @FXML
    private TextField nucleonsAmountField;

    @FXML
    private Button randomizeButton;

    public NucleonsController(CellAutomaton cellAutomaton) {
        this.cellAutomaton = cellAutomaton;
        this.random = new Random();
    }

    @FXML
    private void initialize() {
        nucleonsAmountField.setText(String.valueOf(DEFAULT_NUCLEONS_AMOUNT));
        randomizeButton.setOnMouseClicked(event -> {
            Board board = cellAutomaton.getBoard();
            int nucleonsAmount = Integer.valueOf(nucleonsAmountField.getText());
            for (int i = 0; i < nucleonsAmount; i++) {
                int x = random.nextInt(board.getWidth());
                int y = random.nextInt(board.getHeight());
                Cell cell = board.getCell(new CoordinatePair(x, y));
                if (!cell.isAlive()) {
                    cell.setState(new Cell.State(Cell.Phase.ALIVE, getRandomColor()));
                    cellAutomaton.getGrainsWarehouse().assign(cell);
                }
            }
        });
    }

    private Color getRandomColor() {
        return new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1);
    }

    @Override
    public void reload(CellAutomaton cellAutomaton) {
        this.cellAutomaton = cellAutomaton;
    }
}
