package com.dudududi.grainexpansion.model.definables.neighbourhood;

import com.dudududi.grainexpansion.model.definables.Definable;

/**
 * Created by dudek on 4/21/16.
 */
public abstract class NeighbourhoodType implements Definable {
    private boolean isPeriodic;

    NeighbourhoodType(boolean isPeriodic) {
        this.isPeriodic = isPeriodic;
    }

    public boolean isPeriodic() {
        return isPeriodic;
    }
}
