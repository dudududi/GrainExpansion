package com.dudududi.grainexpansion.model.neighbourhoods;

import com.dudududi.grainexpansion.model.CellNeighbourhood;
import com.dudududi.grainexpansion.model.CoordinatePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 4/28/16.
 */
public class RandomNeighbourhood extends CellNeighbourhood {

    public RandomNeighbourhood(boolean isPeriodic){
        super(isPeriodic);
    }

    @Override
    protected List<CoordinatePair> getCellNeighbourhood() {
        List<CoordinatePair> indices = new ArrayList<>();

        return indices;    }

    @Override
    public String toString(){
        return "Losowe";
    }
}
