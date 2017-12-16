package com.dudududi.grainexpansion.model.nucleation;

import com.dudududi.grainexpansion.model.SimulationModel;

public class IncreasingNucleationType extends ConstantNucleationType {
    private int initialNucleonsAmount;

    @Override
    public void setNucleonsAmount(int amount) {
        initialNucleonsAmount = amount;
        super.setNucleonsAmount(amount);
    }

    @Override
    public void apply(SimulationModel simulationModel) throws InterruptedException{
        super.apply(simulationModel);
        amount += initialNucleonsAmount; // increase
    }

    @Override
    public String toString() {
        return "Increasing";
    }
}
