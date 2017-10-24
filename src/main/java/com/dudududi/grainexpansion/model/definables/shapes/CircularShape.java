package com.dudududi.grainexpansion.model.definables.shapes;

import com.dudududi.grainexpansion.model.CoordinatePair;
import com.dudududi.grainexpansion.model.definables.Definable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 10/23/17.
 */
public class CircularShape implements Definable {
    private final List<CoordinatePair> circularIndices;

    public CircularShape(int radius) {
        this.circularIndices = new ArrayList<>();

        for (int i = -radius/2; i <= radius/2; i++) {
            for (int j = -radius/2; j <= radius/2; j++) {
                if (Math.pow(i, 2) + Math.pow(j, 2) <= Math.pow(radius / 2., 2)) {
                    circularIndices.add(new CoordinatePair(i, j));
                }
            }
        }
    }


    @Override
    public List<CoordinatePair> defineIndices() {
        return circularIndices;
    }

    @Override
    public String toString() {
        return "Circular";
    }
}