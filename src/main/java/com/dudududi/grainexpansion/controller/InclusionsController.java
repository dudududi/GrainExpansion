package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.CellAutomaton;
import com.dudududi.grainexpansion.model.definables.shapes.CircularShape;
import com.dudududi.grainexpansion.model.definables.shapes.Shape;
import com.dudududi.grainexpansion.model.definables.shapes.SquareShape;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class InclusionsController implements Controller {
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

    private CellAutomaton cellAutomaton;

    public InclusionsController(CellAutomaton cellAutomaton) {
        this.cellAutomaton = cellAutomaton;
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
            cellAutomaton.addInclusions(inclusionsAmount, shape);
        });
    }

    @Override
    public void reload(CellAutomaton cellAutomaton) {
        this.cellAutomaton = cellAutomaton;
    }
}
