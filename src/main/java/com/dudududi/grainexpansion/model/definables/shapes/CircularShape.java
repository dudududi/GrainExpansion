package com.dudududi.grainexpansion.model.definables.shapes;

import com.dudududi.grainexpansion.model.cells.CoordinatePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 10/23/17.
 */
public class CircularShape implements Shape {
    private int radius;

    public CircularShape(int radius) {
        this.radius = radius;
    }

    @Override
    public List<CoordinatePair> defineIndices() {
        List<CoordinatePair> circularIndices = new ArrayList<>();
        for (int i = -radius / 2; i <= radius / 2; i++) {
            for (int j = -radius / 2; j <= radius / 2; j++) {
                if (Math.pow(i, 2) + Math.pow(j, 2) <= Math.pow(radius / 2., 2)) {
                    circularIndices.add(new CoordinatePair(i, j));
                }
            }
        }
        return circularIndices;
    }

    @Override
    public String toString() {
        return "Circular";
    }

    @Override
    public void setSize(int size) {
        radius = size;
    }
}