package com.dudududi.grainexpansion.model.rules;

import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.cells.CellState;
import javafx.scene.paint.Color;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dudek on 10/29/17.
 */
public class NearestMoore extends FurtherMoore {
    public NearestMoore(int probability) {
        super(probability);
    }

    @Override
    public CellState shouldCellBeAlive(Cell cell) {
        Map<Color, Long> counts =  cell.getNeighbourhood().stream()
                .filter(c -> c.isAlive() && !c.getState().getType().equals(CellState.Type.INCLUSION))
                .filter(c -> nearestOnly(cell, c))
                .collect(Collectors.groupingBy(Cell::getColor, Collectors.counting()));

        if (counts.size() == 0) return null;
        Color color = counts.entrySet().stream()
                .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .get()
                .getKey();
        long colorCount = counts.get(color);
        if (colorCount == 3) {
            return new CellState(color, CellState.Type.ALIVE);
        }
        return super.shouldCellBeAlive(cell);
    }

    @Override
    public boolean isCellOnBoundary(Cell cell) {
        return false;
    }

    private boolean nearestOnly(Cell origin, Cell neighbour) {
        return  ((Math.abs(origin.getPosition().x - neighbour.getPosition().x) == 1 ^ Math.abs(origin.getPosition().y - neighbour.getPosition().y) == 0) ||
                (Math.abs(origin.getPosition().x - neighbour.getPosition().x) == 0 ^ Math.abs(origin.getPosition().y - neighbour.getPosition().y) == 1));
    }
}
