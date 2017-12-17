package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.SimulationModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.logging.Level;
import java.util.logging.Logger;

public class StatesController extends Controller {
    private static final String DEFAULT_STATES_AMOUNT = "10";

    @FXML
    private TextField statesAmountField;

    @FXML
    private Button generateStatesButton;

    public StatesController(SimulationModel simulationModel) {
        super(simulationModel);
    }

    @FXML
    private void initialize() {
        statesAmountField.setText(DEFAULT_STATES_AMOUNT);
        generateStatesButton.setOnMouseClicked(e -> {
            int statesAmount = Integer.valueOf(statesAmountField.getText());
            new Thread(() -> simulationModel.getMonteCarlo().initialize(statesAmount)).start();
        });
    }
}
