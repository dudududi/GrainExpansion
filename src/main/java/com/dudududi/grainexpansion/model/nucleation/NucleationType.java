package com.dudududi.grainexpansion.model.nucleation;

import com.dudududi.grainexpansion.model.SimulationModel;

public abstract class NucleationType {
    int amount;
    boolean onBoundary;

    public void setNucleonsAmount(int amount) {
        this.amount = amount;
    }

    public void setOnBoundary(boolean onBoundary) {
        this.onBoundary = onBoundary;
    }

    public abstract void apply(SimulationModel simulationModel) throws InterruptedException;
}
