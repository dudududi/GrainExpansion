package com.dudududi.grainexpansion.model.definables.neighbourhood;

import com.dudududi.grainexpansion.model.CoordinatePair;
import com.dudududi.grainexpansion.model.cells.Board;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 4/21/16.
 */
public class MooreNeighbourhood extends NeighbourhoodType {

    public MooreNeighbourhood(Board board) {
        super(board);
    }

    @Override
    public List<CoordinatePair> defineIndices() {
        List<CoordinatePair> neighbourhoodIndices = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) neighbourhoodIndices.add(new CoordinatePair(i, j));
            }
        }
        return neighbourhoodIndices;
    }
}
