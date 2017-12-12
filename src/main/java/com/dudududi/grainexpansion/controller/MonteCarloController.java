package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.SimulationModel;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MonteCarloController extends Controller{

    @FXML
    private SpaceGeneratorController spaceGeneratorController;

    @FXML
    private StatesController statesController;

    @FXML
    private StructuresController structuresController;

    @FXML
    private EnergyDistributionController energyDistributionController;

    @FXML
    private AnimationController animationController;

    private List<Controller> controllerList;

    public MonteCarloController(SimulationModel simulationModel) {
        super(simulationModel);
        this.controllerList = new ArrayList<>();
    }

    @FXML
    private void initialize() {
        controllerList.addAll(Arrays.asList(
                spaceGeneratorController,
                statesController,
                structuresController,
                energyDistributionController,
                animationController
        ));

        controllerList.forEach(controller -> controller.setSimulationMode(SimulationModel.Mode.MONTE_CARLO));
    }

}
