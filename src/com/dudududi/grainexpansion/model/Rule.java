package com.dudududi.grainexpansion.model;

import javafx.scene.paint.Color;

/**
 * Created by dudek on 4/21/16.
 */
public interface Rule {
    Color shouldCellBeAlive(Cell cell);
}
