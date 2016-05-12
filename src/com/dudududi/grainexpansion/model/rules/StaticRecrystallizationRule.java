package com.dudududi.grainexpansion.model.rules;

import com.dudududi.grainexpansion.model.Cell;
import com.dudududi.grainexpansion.model.Rule;
import javafx.scene.paint.Color;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dudek on 4/28/16.
 */
public class StaticRecrystallizationRule implements Rule {
    @Override
    public Color shouldCellBeAlive(Cell cell) {
        if (cell.isAlive()) return null;
        Map<Color, Long> counts =  cell.getNeighbourhood().stream()
                .filter(c -> c.isAlive())
                .collect(Collectors.groupingBy(Cell::getColor, Collectors.counting()));
        if (counts.size() == 0) return null;
        return counts.entrySet().stream()
                .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .get()
                .getKey();
    }
}
