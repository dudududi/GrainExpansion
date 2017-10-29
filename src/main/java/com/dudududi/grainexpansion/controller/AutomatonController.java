package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.CellAutomaton;
import com.dudududi.grainexpansion.model.cells.Board;
import com.dudududi.grainexpansion.model.definables.neighbourhood.MooreNeighbourhood;
import com.dudududi.grainexpansion.model.definables.neighbourhood.NeighbourhoodType;
import com.dudududi.grainexpansion.model.definables.shapes.CircularShape;
import com.dudududi.grainexpansion.model.definables.shapes.Shape;
import com.dudududi.grainexpansion.model.definables.shapes.SquareShape;
import com.dudududi.grainexpansion.model.rules.Rule;
import com.dudududi.grainexpansion.model.rules.StaticRecrystallizationRule;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 10/24/17.
 */
public class AutomatonController implements Controller<CellAutomaton> {
    private static final int DEFAULT_INCLUSIONS_AMOUNT = 8;
    private static final int DEFAULT_INCLUSIONS_SIZE = 8;

    private static final String START_BUTTON_SELECTOR = "#startButton";
    private static final String ADD_INCLUSIONS_BUTTON_SELECTOR = "#addInclusionsButton";
    private static final String GENERATE_BOARD_BUTTON_SELECTOR = "#generateButton";
    private static final String INCLUSIONS_AMOUNT_FIELD_SELECTOR = "#inclusionsAmountField";
    private static final String INCLUSIONS_SIZE_FIELD_SELECTOR = "#inclusionsSizeField";
    private static final String SELECT_INCLUSIONS_TYPE_SELECTOR = "#selectInclusionsType";

    private final ToggleButton startButton;
    private final Button addInclusionsButton;
    private final Button generateBoardButton;
    private final TextField inclusionsAmountField;
    private final TextField inclusionsSizeField;
    private final ComboBox<Shape> selectInclusionsType;

    private CellAutomaton cellAutomaton;

    @SuppressWarnings("unchecked")
    AutomatonController(Scene scene, BoardController boardController) {
        this.startButton = (ToggleButton) scene.lookup(START_BUTTON_SELECTOR);
        this.addInclusionsButton = (Button) scene.lookup(ADD_INCLUSIONS_BUTTON_SELECTOR);
        this.inclusionsAmountField = (TextField) scene.lookup(INCLUSIONS_AMOUNT_FIELD_SELECTOR);
        this.inclusionsSizeField = (TextField) scene.lookup(INCLUSIONS_SIZE_FIELD_SELECTOR);
        this.selectInclusionsType = (ComboBox) scene.lookup(SELECT_INCLUSIONS_TYPE_SELECTOR);
        this.generateBoardButton = (Button) scene.lookup(GENERATE_BOARD_BUTTON_SELECTOR);

        this.cellAutomaton = new CellAutomaton(boardController.getBoard());

        generateBoardButton.setOnMouseClicked(event -> {
            int width = Integer.valueOf(boardWidthField.getText());
            int height = Integer.valueOf(boardHeightField.getText());
            boolean isPeriodic = periodicBC.selectedProperty().get();
            board = new Board(width, height, isPeriodic);
            bindAutomatonWithBoard();
        });

        configureAutomationAnimation();
        configureInclusions();
    }

    private void configureAutomationAnimation() {
        NeighbourhoodType neighbourhoodType = new MooreNeighbourhood(cellAutomaton.getBoard());
        Rule automatonRule = new StaticRecrystallizationRule(neighbourhoodType);
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(90), event -> cellAutomaton.next(automatonRule)));
        animation.setCycleCount(Timeline.INDEFINITE);
        startButton.setToggleGroup(new ToggleGroup());
        startButton.getToggleGroup().selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                animation.stop();
                //disableNodes.forEach(node -> node.setDisable(false));
                startButton.setText("Start");
            } else {
                animation.play();
                //disableNodes.forEach(node -> node.setDisable(true));
                startButton.setText("Stop");
            }
        });
    }

    private void configureInclusions(){
        inclusionsAmountField.setText(String.valueOf(DEFAULT_INCLUSIONS_AMOUNT));
        inclusionsSizeField.setText(String.valueOf(DEFAULT_INCLUSIONS_SIZE));
        selectInclusionsType.getItems().addAll(new SquareShape(DEFAULT_INCLUSIONS_SIZE),
                new CircularShape(DEFAULT_INCLUSIONS_SIZE));
        selectInclusionsType.getSelectionModel().selectFirst();

        addInclusionsButton.setOnMouseClicked(event -> {
            int inclusionsAmount = Integer.valueOf(inclusionsAmountField.getText());
            int size = Integer.valueOf(inclusionsSizeField.getText());
            Shape shape = selectInclusionsType.getSelectionModel().getSelectedItem();
            shape.setSize(size);
            cellAutomaton.addInclusions(inclusionsAmount, shape);
        });
    }

    @Override
    @SuppressWarnings("Duplicates")
    public List<Node> collectDisablingNodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(startButton);
        nodes.add(addInclusionsButton);
        nodes.add(inclusionsAmountField);
        nodes.add(inclusionsSizeField);
        nodes.add(selectInclusionsType);
        nodes.add(generateBoardButton);
        return nodes;
    }

    @Override
    public void reload(CellAutomaton newAutomaton) {
        cellAutomaton = newAutomaton;
        configureAutomationAnimation();
    }

    CellAutomaton getCellAutomaton(){
        return cellAutomaton;
    }
}
