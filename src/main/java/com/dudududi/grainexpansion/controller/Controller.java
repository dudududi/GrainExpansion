package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.Observer;
import com.dudududi.grainexpansion.model.SimulationModel;
import com.dudududi.grainexpansion.model.cells.Cell;

/**
 * Created by dudek on 10/24/17.
 */
public abstract class Controller implements Observer{
    protected SimulationModel simulationModel;
    protected SimulationModel.Mode simulationMode;

    public Controller(SimulationModel simulationModel) {
        this.simulationModel = simulationModel;
        this.simulationModel.attach(this);
    }

    void setSimulationMode(SimulationModel.Mode simulationMode) {
        this.simulationMode = simulationMode;
    }

    @Override
    public void onBoardChanged() {
        // do not enforce handling this event by each controller
    }

    @Override
    public void onAliveCellClicked(Cell cell) {
        // do not enforce handling this event by each controller
    }
}
