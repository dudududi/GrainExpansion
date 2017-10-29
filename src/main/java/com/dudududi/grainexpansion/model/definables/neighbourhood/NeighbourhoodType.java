package com.dudududi.grainexpansion.model.definables.neighbourhood;

import com.dudududi.grainexpansion.model.cells.Board;
import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.definables.Definable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by dudek on 4/21/16.
 */
public abstract class NeighbourhoodType implements Definable {
    private final Board board;

    public NeighbourhoodType(Board board) {
        this.board = board;
    }

    public Stream<Cell> getNeighbourhood(Cell origin) {
        return defineIndices().stream()
                .map(relativePos -> board.calculateAbsolutePosition(relativePos, origin))
                .filter(Objects::nonNull)
                .map(board::getCell);
    }
}
