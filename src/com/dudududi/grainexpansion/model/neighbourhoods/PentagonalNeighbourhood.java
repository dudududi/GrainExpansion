package com.dudududi.grainexpansion.model.neighbourhoods;

import com.dudududi.grainexpansion.model.CellNeighbourhood;
import com.dudududi.grainexpansion.model.CoordinatePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dudek on 4/28/16.
 */
public class PentagonalNeighbourhood extends CellNeighbourhood {
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private Random random;
    public PentagonalNeighbourhood(boolean isPeriodic){
        super(isPeriodic);
        random = new Random();
    }
    @Override
    protected List<CoordinatePair> getCellNeighbourhood() {
        int side = random.nextInt(4);
        List<CoordinatePair> indices = new ArrayList<>();
        switch (side){
            case LEFT:
                indices.add(new CoordinatePair(1, 0));
                indices.add(new CoordinatePair(1, -1));
                indices.add(new CoordinatePair(0, -1));
                indices.add(new CoordinatePair(-1, -1));
                indices.add(new CoordinatePair(-1, 0));
                break;
            case RIGHT:
                indices.add(new CoordinatePair(1, 0));
                indices.add(new CoordinatePair(1, 1));
                indices.add(new CoordinatePair(0, 1));
                indices.add(new CoordinatePair(-1, 1));
                indices.add(new CoordinatePair(-1, 0));
                break;
            case UP:
                indices.add(new CoordinatePair(-1, 0));
                indices.add(new CoordinatePair(-1, 1));
                indices.add(new CoordinatePair(0, 1));
                indices.add(new CoordinatePair(1, 1));
                indices.add(new CoordinatePair(1, 0));
                break;
            case DOWN:
                indices.add(new CoordinatePair(-1, 0));
                indices.add(new CoordinatePair(-1, -1));
                indices.add(new CoordinatePair(0, -1));
                indices.add(new CoordinatePair(1, -1));
                indices.add(new CoordinatePair(1, 0));
                break;
        }
        return indices;
    }

    @Override
    public String toString(){
        return "Pentagonalne losowe";
    }
}
