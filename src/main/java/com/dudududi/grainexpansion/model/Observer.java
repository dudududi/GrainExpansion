package com.dudududi.grainexpansion.model;

import com.dudududi.grainexpansion.model.cells.Cell;

public interface Observer {
    void onBoardChanged();
    void onAliveCellClicked(Cell cell);
}
