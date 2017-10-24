package com.dudududi.grainexpansion.model.definables.neighbourhood;

import com.dudududi.grainexpansion.model.CoordinatePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 4/21/16.
 */
public class MooreNeighbourhood extends NeighbourhoodType {

    public MooreNeighbourhood(boolean isPeriodic) {
        super(isPeriodic);
    }

    @Override
    public List<CoordinatePair> defineIndices() {
        List<CoordinatePair> indices = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) indices.add(new CoordinatePair(i, j));
            }
        }
        return indices;
    }
}
