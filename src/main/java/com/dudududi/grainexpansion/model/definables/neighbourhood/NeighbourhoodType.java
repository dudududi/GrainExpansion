package com.dudududi.grainexpansion.model.definables.neighbourhood;

import com.dudududi.grainexpansion.model.cells.Board;
import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.definables.Definable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by dudek on 4/21/16.
 */
public abstract class NeighbourhoodType implements Definable {
    private final Map<Cell, List<Cell>> neighboursMap;

    NeighbourhoodType(final Board board) {
        this.neighboursMap = new HashMap<>();
        board.getCells().forEach(cell -> findNeighbourhood(cell, board));
    }

    public List<Cell> getNeighbourhood(final Cell origin) {
        return neighboursMap.get(origin);
    }

    private void findNeighbourhood(final Cell origin, final Board board) {
        List<Cell> neighbourhood = this.defineIndices()
                .stream()
                .map(relativePos -> board.calculateAbsolutePosition(relativePos, origin))
                .filter(Objects::nonNull)
                .map(board::getCell)
                .collect(Collectors.toList());
        neighboursMap.put(origin, neighbourhood);
    }
}
