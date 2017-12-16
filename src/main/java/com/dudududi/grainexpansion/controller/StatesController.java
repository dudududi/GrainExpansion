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
            StatesUpdater statesUpdater = new StatesUpdater(simulationModel, statesAmount);
            new Thread(statesUpdater).start();
        });
    }

    private static class StatesUpdater implements Runnable {
        private final SimulationModel simulationModel;
        private final int statesAmount;
        private StatesUpdater(SimulationModel simulationModel, int statesAmount) {
            this.simulationModel = simulationModel;
            this.statesAmount = statesAmount;
        }

        @Override
        public void run() {
            try {
                simulationModel.getMonteCarlo().initialize(statesAmount);
            } catch (InterruptedException e) {
                Logger.getGlobal().log(Level.ALL, "StatesUpdater has been interrupted. {0}", e);
            }
        }
    }

}
