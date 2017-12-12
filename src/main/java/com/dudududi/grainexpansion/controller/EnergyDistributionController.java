package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.SimulationModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class EnergyDistributionController extends Controller {
    private static final String DEFAULT_ENERGY_VALUE = "5";
    private static final String DEFAULT_BOUNDARY_ENERGY_VALUE = "8";
    private static final String HOMOGENEOUS = "Homogeneous";
    private static final String HETEROGENEOUS = "Heterogeneous";

    @FXML
    private ComboBox<String> selectEnergyDistType;

    @FXML
    private TextField energyField;

    @FXML
    private TextField boundaryEnergyField;

    @FXML
    private Label boundaryEnergyLabel;

    @FXML
    private Button distributeEnergyButton;

    public EnergyDistributionController(SimulationModel simulationModel) {
        super(simulationModel);
    }

    @FXML
    private void initialize() {
        energyField.setText(DEFAULT_ENERGY_VALUE);
        boundaryEnergyField.setText(DEFAULT_BOUNDARY_ENERGY_VALUE);
        selectEnergyDistType.getItems().addAll(HOMOGENEOUS, HETEROGENEOUS);
        selectEnergyDistType.getSelectionModel().selectFirst();
        selectEnergyDistType.valueProperty().addListener(((observable, oldValue, newValue) ->
                setBoundaryEnergyOptionsVisible(newValue.equals(HETEROGENEOUS))
        ));

        distributeEnergyButton.setOnMouseClicked(this::distributeEnergy);
        boundaryEnergyLabel.managedProperty().bind(boundaryEnergyLabel.visibleProperty());
        boundaryEnergyField.managedProperty().bind(boundaryEnergyField.visibleProperty());
        setBoundaryEnergyOptionsVisible(false);
    }

    private void setBoundaryEnergyOptionsVisible(boolean visible) {
        boundaryEnergyLabel.setVisible(visible);
        boundaryEnergyField.setVisible(visible);
    }

    private void distributeEnergy(MouseEvent event) {
        String selectedDistType = selectEnergyDistType.getSelectionModel().getSelectedItem();
        int energy = Integer.valueOf(energyField.getText());
        int boundary = -1;
        if (selectedDistType.equals(HETEROGENEOUS)) {
            boundary = Integer.valueOf(boundaryEnergyField.getText());
        }
        simulationModel.distributeEnergy(energy, boundary);
    }
}
