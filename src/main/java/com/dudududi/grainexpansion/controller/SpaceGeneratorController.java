package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.CellAutomaton;
import com.dudududi.grainexpansion.model.cells.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class SpaceGeneratorController implements Controller {
    public static final int DEFAULT_WIDTH = 400;
    public static final int DEFAULT_HEIGHT = 400;
    public static final boolean DEFAULT_PERIODICITY = true;

    private CellAutomaton cellAutomaton;

    @FXML
    private Button generateButton;

    @FXML
    private TextField boardWidth;

    @FXML
    private TextField boardHeight;

    @FXML
    private CheckBox periodicBC;

    public SpaceGeneratorController(CellAutomaton cellAutomaton) {
        this.cellAutomaton = cellAutomaton;
    }

    @FXML
    private void initialize() {
        boardWidth.setText(Integer.toString(DEFAULT_WIDTH));
        boardHeight.setText(Integer.toString(DEFAULT_HEIGHT));
        periodicBC.setSelected(DEFAULT_PERIODICITY);

        generateButton.setOnMouseClicked(event -> {
            int width = Integer.valueOf(boardWidth.getText());
            int height = Integer.valueOf(boardHeight.getText());
            boolean isPeriodic = periodicBC.selectedProperty().get();
            cellAutomaton.reinitializeWithBoard(new Board(width, height, isPeriodic));
        });
    }

    @Override
    public void reload(CellAutomaton cellAutomaton) {
        this.cellAutomaton = cellAutomaton;
        boardWidth.setText(String.valueOf(cellAutomaton.getBoard().getWidth()));
        boardHeight.setText(String.valueOf(cellAutomaton.getBoard().getHeight()));
    }
}
