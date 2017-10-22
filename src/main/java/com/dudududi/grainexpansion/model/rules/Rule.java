package com.dudududi.grainexpansion.model.rules;

import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.cells.CellState;

/**
 * Created by dudek on 4/21/16.
 */
public interface Rule {
    CellState shouldCellBeAlive(Cell cell);

    boolean isCellOnBoundary(Cell cell);
}
