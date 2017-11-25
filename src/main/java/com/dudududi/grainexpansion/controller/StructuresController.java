package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.CellAutomaton;
import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.cells.GrainsWarehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class StructuresController implements Controller{
    private static final String DEFAULT_BOUNDARIES_SIZE = "5";

    @FXML
    private ComboBox<StructureType> selectStructureType;

    @FXML
    private TextField boundariesSizeField;

    @FXML
    private Label boundariesSizeLabel;

    @FXML
    private Button colorAllBoundariesButton;


    private CellAutomaton cellAutomaton;

    public StructuresController(CellAutomaton cellAutomaton) {
        this.cellAutomaton = cellAutomaton;
    }

    @FXML
    private void initialize() {
        boundariesSizeField.setText(DEFAULT_BOUNDARIES_SIZE);
        setBoundariesOptionsVisible(false);

        selectStructureType.getItems().addAll(
                StructureType.NONE,
                StructureType.DUAL_PHASE,
                StructureType.SUBSTRUCTURE,
                StructureType.BOUNDARY);
        selectStructureType.getSelectionModel().selectFirst();
        selectStructureType.valueProperty().addListener((observable, oldValue, newValue) -> {
            setBoundariesOptionsVisible(newValue == StructureType.BOUNDARY);
        });

        colorAllBoundariesButton.setOnMouseClicked(event -> {
            int size = Integer.valueOf(boundariesSizeField.getText());
            cellAutomaton.getGrainsWarehouse().changeAllBoundariesState(size);
        });
    }

    @Override
    public void reload(CellAutomaton cellAutomaton) {
        this.cellAutomaton = cellAutomaton;
    }

    void onAliveCellClicked(Cell cell) {
        if (cell.isExcluded()) {
            return;
        }
        StructureType type = selectStructureType.getSelectionModel().getSelectedItem();
        GrainsWarehouse grainsWarehouse = cellAutomaton.getGrainsWarehouse();
        switch (type) {
            case DUAL_PHASE:
                grainsWarehouse.changeGrainState(cell, new Cell.State(Cell.Phase.SUB_STRUCTURE, Color.MAGENTA));
                break;
            case SUBSTRUCTURE:
                grainsWarehouse.changeGrainState(cell, new Cell.State(Cell.Phase.SUB_STRUCTURE, cell.colorProperty().get()));
                break;
            case BOUNDARY:
                grainsWarehouse.changeGrainBoundariesState(cell, Integer.parseInt(boundariesSizeField.getText()));
                break;
            default:
                // do nothing...
        }
    }

    private enum StructureType {
        NONE("None"),
        DUAL_PHASE ("Dual phase"),
        SUBSTRUCTURE ("Substructure"),
        BOUNDARY ("Boundary");

        private final String name;
        StructureType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private void setBoundariesOptionsVisible(boolean visible) {
        boundariesSizeLabel.setVisible(visible);
        boundariesSizeField.setVisible(visible);
        colorAllBoundariesButton.setVisible(visible);
    }
}
