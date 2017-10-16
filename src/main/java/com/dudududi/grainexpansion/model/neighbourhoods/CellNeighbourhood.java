package com.dudududi.grainexpansion.model.neighbourhoods;

import com.dudududi.grainexpansion.model.CoordinatePair;

import java.util.List;

/**
 * Created by dudek on 4/21/16.
 */
public abstract class CellNeighbourhood {
    private boolean isPeriodic;

    CellNeighbourhood() {
        isPeriodic = true;
    }

    public boolean isPeriodic() {
        return isPeriodic;
    }

    public void setPeriodic(boolean isPeriodic){
        this.isPeriodic = isPeriodic;
    }

    public abstract List<CoordinatePair> getCellNeighbourhood();
}
