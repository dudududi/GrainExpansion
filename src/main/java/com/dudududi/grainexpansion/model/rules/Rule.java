package com.dudududi.grainexpansion.model.rules;

import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.definables.neighbourhood.NeighbourhoodType;

/**
 * Created by dudek on 4/21/16.
 */
public interface Rule {
    Cell.State determineState(Cell cell, NeighbourhoodType neighbourhoodType);
}
