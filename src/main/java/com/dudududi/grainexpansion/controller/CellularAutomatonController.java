package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.CellAutomaton;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CellularAutomatonController implements Controller{

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

    private CellAutomaton cellAutomaton;
    private List<Controller> controllerList;


    public CellularAutomatonController(CellAutomaton cellAutomaton) {
        this.cellAutomaton = cellAutomaton;
        this.controllerList = new ArrayList<>();
    }

    void setBoardController(BoardController boardController) {
        boardController.setStructuresController(structuresController);
    }

    @FXML
    private void initialize(){
        controllerList.addAll(Arrays.asList(
                animationController,
                inclusionsController,
                nucleonsController,
                spaceGeneratorController,
                structuresController));

        animationController.setStructuresController(structuresController);
    }

    @Override
    public void reload(CellAutomaton cellAutomaton) {
            this.cellAutomaton = cellAutomaton;
            reloadAllControllers();
    }


    private void reloadAllControllers() {
        controllerList.forEach(controller -> controller.reload(cellAutomaton));
    }

}
