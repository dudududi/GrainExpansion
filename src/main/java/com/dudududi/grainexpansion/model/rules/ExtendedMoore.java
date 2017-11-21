package com.dudududi.grainexpansion.model.rules;

import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.cells.CellState;
import javafx.scene.paint.Color;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dudek on 10/29/17.
 */
public class ExtendedMoore extends NearestMoore {

    @Override
    public CellState shouldCellBeAlive(Cell cell) {
        if (cell.isAlive() || cell.getState().getType().equals(CellState.Type.INCLUSION)) return null;
        Map<Color, Long> counts =  cell.getNeighbourhood().stream()
                .filter(c -> c.isAlive() && !c.getState().getType().equals(CellState.Type.INCLUSION))
                .collect(Collectors.groupingBy(Cell::getColor, Collectors.counting()));
        if (counts.size() == 0) return null;
        Color color = counts.entrySet().stream()
                .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .get()
                .getKey();
        long colorCount = counts.get(color);
        if (colorCount >=5) {
            return new CellState(color, CellState.Type.ALIVE);
        }
        return super.shouldCellBeAlive(cell);
    }

    @Override
    public String toString(){
        return "Extended";
    }

}
