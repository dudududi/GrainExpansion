package com.dudududi.grainexpansion.model.definables.shapes;

import com.dudududi.grainexpansion.model.CoordinatePair;
import com.dudududi.grainexpansion.model.definables.Definable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 10/23/17.
 */
public class SquareShape implements Definable{
    private final List<CoordinatePair> squareIndices;

    public SquareShape(int diagonal) {
        this.squareIndices = new ArrayList<>();

        for (int i = -diagonal/2; i <= diagonal/2; i++) {
            for (int j = -diagonal/2; j <= diagonal/2; j++) {
                squareIndices.add(new CoordinatePair(i, j));
            }
        }
    }


    @Override
    public List<CoordinatePair> defineIndices() {
        return squareIndices;
    }

    @Override
    public String toString() {
        return "Square";
    }
}
