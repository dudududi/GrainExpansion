package com.dudududi.grainexpansion.model.rules;

import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.cells.CellState;
import javafx.scene.paint.Color;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dudek on 4/28/16.
 */
@SuppressWarnings("all") //TODO: to be removed and refactored
public class StaticRecrystallizationRule implements Rule {
    @Override
    public CellState shouldCellBeAlive(Cell cell) {
        if (cell.isAlive()) return null;
        Map<Color, Long> counts =  cell.getNeighbourhood().stream()
                .filter(c -> c.isAlive())
                .collect(Collectors.groupingBy(Cell::getColor, Collectors.counting()));
        if (counts.size() == 0) return null;
        Color color = counts.entrySet().stream()
                .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .get()
                .getKey();
        return new CellState(color, CellState.Type.ALIVE);
    }
}
