package com.dudududi.grainexpansion.model.nucleation;

import com.dudududi.grainexpansion.model.SimulationModel;

public class InitialNucleationType extends ConstantNucleationType {

    private boolean wasApplied;

    @Override
    public void setNucleonsAmount(int amount) {
        wasApplied = false;
    }

    @Override
    public void apply(SimulationModel simulationModel) throws InterruptedException{
        if (!wasApplied) {
            wasApplied = true;
            super.apply(simulationModel);
        }
    }

    @Override
    public String toString() {
        return "At the beginning";
    }
}
