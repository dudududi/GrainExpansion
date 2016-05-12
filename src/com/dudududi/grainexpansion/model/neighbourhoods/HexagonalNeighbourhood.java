package com.dudududi.grainexpansion.model.neighbourhoods;

import com.dudududi.grainexpansion.model.CellNeighbourhood;
import com.dudududi.grainexpansion.model.CoordinatePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 4/28/16.
 */
public class HexagonalNeighbourhood extends CellNeighbourhood {
    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    private int whichSide;

    public HexagonalNeighbourhood(boolean isPeriodic, int whichSide){
        super(isPeriodic);
        this.whichSide = whichSide;
    }

    @Override
    protected List<CoordinatePair> getCellNeighbourhood() {
        List<CoordinatePair> indices = new ArrayList<>();
        switch (whichSide){
            case RIGHT:
                indices.add(new CoordinatePair(-1, 0));
                indices.add(new CoordinatePair(-1, 1));
                indices.add(new CoordinatePair(0, 1));
                indices.add(new CoordinatePair(1, 0));
                indices.add(new CoordinatePair(1, -1));
                indices.add(new CoordinatePair(0, -1));
                break;
            case LEFT:
                indices.add(new CoordinatePair(0, 1));
                indices.add(new CoordinatePair(1, 1));
                indices.add(new CoordinatePair(1, 0));
                indices.add(new CoordinatePair(0, -1));
                indices.add(new CoordinatePair(-1, -1));
                indices.add(new CoordinatePair(-1, 0));
                break;
        }
        return indices;
    }

    @Override
    public String toString(){
        String description = "Heksadecymalne ";
        description += whichSide == LEFT ? "lewe" : "prawe";
        return description;
    }
}
