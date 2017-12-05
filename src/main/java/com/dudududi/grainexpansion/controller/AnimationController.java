package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.SimulationModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Duration;

public class AnimationController extends Controller {

    @FXML
    private ToggleButton startButton;

    @FXML
    private Button clearButton;

    public AnimationController(SimulationModel simulationModel) {
        super(simulationModel);
    }

    @FXML
    private void initialize() {
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(200), event ->
            simulationModel.next(simulationMode)
        ));
        animation.setCycleCount(Timeline.INDEFINITE);
        startButton.setToggleGroup(new ToggleGroup());
        startButton.getToggleGroup().selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                animation.stop();
                startButton.setText("Start");
            } else {
                animation.play();
                startButton.setText("Stop");
            }
        });

        clearButton.setOnMouseClicked(event -> simulationModel.clear(false));
    }
}
