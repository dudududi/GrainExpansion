package com.dudududi.grainexpansion.model.definables.shapes;

import com.dudududi.grainexpansion.model.CoordinatePair;
import com.dudududi.grainexpansion.model.definables.Definable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 10/23/17.
 */
public class SquareShape implements Shape{
    private int diagonal;

    public SquareShape(int diagonal) {
        this.diagonal = diagonal;
    }


    @Override
    public List<CoordinatePair> defineIndices() {
        List<CoordinatePair> squareIndices = new ArrayList<>();
        for (int i = -diagonal/2; i <= diagonal/2; i++) {
            for (int j = -diagonal/2; j <= diagonal/2; j++) {
                squareIndices.add(new CoordinatePair(i, j));
            }
        }
        return squareIndices;
    }

    @Override
    public String toString() {
        return "Square";
    }

    @Override
    public void setSize(int size) {
        this.diagonal = size;
    }
}
