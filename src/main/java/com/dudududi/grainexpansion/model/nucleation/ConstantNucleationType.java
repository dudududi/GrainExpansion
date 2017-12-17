package com.dudududi.grainexpansion.model.nucleation;

import com.dudududi.grainexpansion.model.SimulationModel;
import com.dudududi.grainexpansion.model.cells.Cell;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ConstantNucleationType extends NucleationType {
    private Random random;

    public ConstantNucleationType() {
        this.random = new Random();
    }

    @Override
    public void apply(SimulationModel simulationModel) throws InterruptedException {
        List<Cell> cells = (onBoundary ? simulationModel.getGrainsWarehouse().findBoundary() :
                simulationModel.getBoard().getCells())
                .stream()
                .filter(cell -> !cell.isRecrystallized())
                .collect(Collectors.toList());

        if (cells.isEmpty()) {
            return;
        }
        for (int i = 0; i < amount; i++) {
            Cell cell = cells.get(random.nextInt(cells.size() - 1));
            simulationModel.getBoard().updateCellState(cell, Cell.State.createRecrystallizedState());
            simulationModel.getGrainsWarehouse().assign(cell);
        }
    }

    @Override
    public String toString() {
        return "Constant";
    }
}
