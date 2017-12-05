package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.SimulationModel;
import com.dudududi.grainexpansion.model.models.CellAutomaton;
import com.dudududi.grainexpansion.model.cells.CoordinatePair;
import com.dudududi.grainexpansion.model.cells.Board;
import com.dudududi.grainexpansion.model.cells.Cell;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.Random;

public class NucleonsController extends Controller {
    private static final int DEFAULT_NUCLEONS_AMOUNT = 25;

    private Random random;

    @FXML
    private TextField nucleonsAmountField;

    @FXML
    private Button randomizeButton;

    public NucleonsController(SimulationModel simulationModel) {
        super(simulationModel);
        this.random = new Random();
    }

    @FXML
    private void initialize() {
        nucleonsAmountField.setText(String.valueOf(DEFAULT_NUCLEONS_AMOUNT));
        randomizeButton.setOnMouseClicked(event -> {
            Board board = simulationModel.getBoard();
            int nucleonsAmount = Integer.valueOf(nucleonsAmountField.getText());
            for (int i = 0; i < nucleonsAmount; i++) {
                int x = random.nextInt(board.getWidth());
                int y = random.nextInt(board.getHeight());
                Cell cell = board.getCell(new CoordinatePair(x, y));
                if (cell.isDead()) {
                    simulationModel.addNucleon(cell);
                }
            }
        });
    }
}
