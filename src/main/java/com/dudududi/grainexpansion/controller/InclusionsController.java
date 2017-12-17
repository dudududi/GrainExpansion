package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.SimulationModel;
import com.dudududi.grainexpansion.model.definables.shapes.CircularShape;
import com.dudududi.grainexpansion.model.definables.shapes.Shape;
import com.dudududi.grainexpansion.model.definables.shapes.SquareShape;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class InclusionsController extends Controller {
    private static final int DEFAULT_INCLUSIONS_AMOUNT = 8;
    private static final int DEFAULT_INCLUSIONS_SIZE = 8;

    @FXML
    private TextField inclusionsAmountField;

    @FXML
    private TextField inclusionsSizeField;

    @FXML
    private ComboBox<Shape> selectInclusionsType;

    @FXML
    private Button addInclusionsButton;

    public InclusionsController(SimulationModel simulationModel) {
        super(simulationModel);
    }

    @FXML
    private void initialize() {
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
            new Thread(() -> simulationModel.addInclusions(inclusionsAmount, shape)).start();
        });
    }
}
