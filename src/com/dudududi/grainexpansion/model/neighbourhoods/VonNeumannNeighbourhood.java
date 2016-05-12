package com.dudududi.grainexpansion.model.neighbourhoods;

import com.dudududi.grainexpansion.model.CellNeighbourhood;
import com.dudududi.grainexpansion.model.CoordinatePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dudek on 4/28/16.
 */
public class VonNeumannNeighbourhood extends CellNeighbourhood {

    public VonNeumannNeighbourhood(boolean isPeriodic){
        super(isPeriodic);
    }

    @Override
    protected List<CoordinatePair> getCellNeighbourhood() {
        List<CoordinatePair> indices = new ArrayList<>();
        indices.add(new CoordinatePair(-1, 0));
        indices.add(new CoordinatePair(1, 0));
        indices.add(new CoordinatePair(0, -1));
        indices.add(new CoordinatePair(0, 1));
        return indices;
    }

    @Override
    public String toString(){
        return "Von Neumann'a";
    }
}
