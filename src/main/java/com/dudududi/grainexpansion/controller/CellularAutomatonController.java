package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.SimulationModel;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CellularAutomatonController extends Controller{

    // Controllers list
    @FXML
    @SuppressWarnings("UnusedDeclaration")
    private AnimationController animationController;

    @FXML @SuppressWarnings("UnusedDeclaration")
    private InclusionsController inclusionsController;

    @FXML @SuppressWarnings("UnusedDeclaration")
    private NucleonsController nucleonsController;

    @FXML @SuppressWarnings("UnusedDeclaration")
    private SpaceGeneratorController spaceGeneratorController;

    @FXML @SuppressWarnings("UnusedDeclaration")
    private StructuresController structuresController;

    private List<Controller> controllerList;


    public CellularAutomatonController(SimulationModel simulationModel) {
        super(simulationModel);
        this.controllerList = new ArrayList<>();
    }

    @FXML
    private void initialize(){
        controllerList.addAll(Arrays.asList(
                animationController,
                inclusionsController,
                nucleonsController,
                spaceGeneratorController,
                structuresController));

        controllerList.forEach(controller -> controller.setSimulationMode(SimulationModel.Mode.CELLULAR_AUTOMATON));
    }

}
