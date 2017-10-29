package com.dudududi.grainexpansion.model.rules;

import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.definables.neighbourhood.NeighbourhoodType;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Created by dudek on 4/28/16.
 */
public class StaticRecrystallizationRule implements Rule {

    private NeighbourhoodType neighbourhoodType;

    public StaticRecrystallizationRule(NeighbourhoodType neighbourhoodType) {
        this.neighbourhoodType = neighbourhoodType;
    }

    @Override
    public Cell.State determineState(Cell cell) {
        if (cell.isAlive() || cell.isInclusion()) return null;

        Optional<Cell.State> mostOccurredState = neighbourhoodType.getNeighbourhood(cell)
                .filter(c -> c.isAlive() && !c.isInclusion())
                .collect(Collectors.groupingBy(Cell::getState, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey);

        if (mostOccurredState.isPresent()) {
            return Cell.State.copyState(mostOccurredState.get());
        }
        return null;
    }
}
