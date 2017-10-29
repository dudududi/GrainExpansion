package com.dudududi.grainexpansion.model.rules;

import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.cells.CellState;
import javafx.scene.paint.Color;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dudek on 10/29/17.
 */
public class FurtherMoore extends StaticRecrystallizationRule{
    public FurtherMoore(int probability) {
        super(probability);
    }

    @Override
    public CellState shouldCellBeAlive(Cell cell) {
        Map<Color, Long> counts =  cell.getNeighbourhood().stream()
                .filter(c -> c.isAlive() && !c.getState().getType().equals(CellState.Type.INCLUSION))
                .filter(c -> furtherOnly(cell, c))
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

    private boolean furtherOnly(Cell origin, Cell neighbour) {
        return  ((Math.abs(origin.getPosition().x - neighbour.getPosition().x) == 1 && Math.abs(origin.getPosition().y - neighbour.getPosition().y) == 1));
    }
}
