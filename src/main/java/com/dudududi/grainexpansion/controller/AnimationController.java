package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.SimulationModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Duration;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AnimationController extends Controller {

    private Thread simulationThread;

    @FXML
    private ToggleButton startButton;

    @FXML
    private Button clearButton;

    public AnimationController(SimulationModel simulationModel) {
        super(simulationModel);
    }

    @FXML
    private void initialize() {
        startButton.setToggleGroup(new ToggleGroup());
        startButton.getToggleGroup().selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                startButton.setText("Start");
                simulationThread.interrupt();
            } else {
                simulationThread = new Thread(new SimulationTask(simulationModel, simulationMode));
                simulationThread.start();
                startButton.setText("Stop");
            }
        });

        clearButton.setOnMouseClicked(event -> simulationModel.clear(false));
    }

    private static class SimulationTask implements Runnable {
        private SimulationModel simulationModel;
        private SimulationModel.Mode mode;
        private SimulationTask(SimulationModel simulationModel, SimulationModel.Mode simulationMode) {
            this.simulationModel = simulationModel;
            this.mode = simulationMode;
        }
        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    simulationModel.next(mode);
                }
            } catch (InterruptedException e) {
                Logger.getGlobal().log(Level.ALL, "SimulationTask has been interrupted. {0}", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
