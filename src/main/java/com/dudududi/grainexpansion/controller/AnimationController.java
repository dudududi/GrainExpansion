package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.CellAutomaton;
import com.dudududi.grainexpansion.model.rules.BasicRule;
import com.dudududi.grainexpansion.model.rules.Rule;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Duration;

public class AnimationController implements Controller {

    @FXML
    private ToggleButton startButton;

    @FXML
    private Button clearButton;

    private CellAutomaton cellAutomaton;
    private StructuresController structuresController;

    public AnimationController(CellAutomaton cellAutomaton) {
        this.cellAutomaton = cellAutomaton;
    }

    @FXML
    private void initialize() {
        Rule automatonRule = new BasicRule();
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(100), event -> cellAutomaton.next(automatonRule)));
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

        clearButton.setOnMouseClicked(event -> cellAutomaton.clear(structuresController.shouldClearAll()));
    }

    void setStructuresController(StructuresController structuresController) {
        this.structuresController = structuresController;
    }

    @Override
    public void reload(CellAutomaton cellAutomaton) {
        this.cellAutomaton = cellAutomaton;
    }

}
