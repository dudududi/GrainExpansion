package com.dudududi.grainexpansion.model.definables;

import com.dudududi.grainexpansion.model.CoordinatePair;

import java.util.List;

/**
 * Created by dudek on 10/23/17.
 */
public interface Definable {
    List<CoordinatePair> defineIndices();
}
