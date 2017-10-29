package com.dudududi.grainexpansion.model.definables.neighbourhood;

import com.dudududi.grainexpansion.model.CoordinatePair;
import com.dudududi.grainexpansion.model.cells.Board;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 4/21/16.
 */
public class MooreNeighbourhood extends NeighbourhoodType {

    private final List<CoordinatePair> neighbourhoodIndices;

    public MooreNeighbourhood(Board board) {
        super(board);
        neighbourhoodIndices = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) neighbourhoodIndices.add(new CoordinatePair(i, j));
            }
        }
    }

    @Override
    public List<CoordinatePair> defineIndices() {
        return neighbourhoodIndices;
    }
}
