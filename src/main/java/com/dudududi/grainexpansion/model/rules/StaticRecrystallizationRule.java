package com.dudududi.grainexpansion.model.rules;

import com.dudududi.grainexpansion.model.cells.Cell;
import javafx.scene.paint.Color;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dudek on 4/28/16.
 */
public class StaticRecrystallizationRule implements Rule {
    @Override
    public Cell.State determineState(Cell cell) {
        if (cell.isAlive() || cell.getState().getType().equals(CellState.Type.INCLUSION)) return null;
        Map<Color, Long> counts =  cell.getNeighbourhood().stream()
                .filter(c -> c.isAlive() && !c.getState().getType().equals(CellState.Type.INCLUSION))
                .collect(Collectors.groupingBy(Cell::getColor, Collectors.counting()));
        if (counts.size() == 0) return null;
        Color color = counts.entrySet().stream()
                .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .get()
                .getKey();
        return new CellState(color, CellState.Type.ALIVE);
    }

    @Override
    public boolean isCellOnBoundary(Cell cell) {
        return cell.getNeighbourhood().stream()
                .anyMatch(c -> !(c.getColor().equals(cell.getColor()) && cell.isAlive()));
    }
}
