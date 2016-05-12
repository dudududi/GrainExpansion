package com.dudududi.grainexpansion.controller;

import com.dudududi.grainexpansion.model.CellAutomaton;

/**
 * Created by dudek on 5/7/16.
 */
public interface Randomizable {
    void randomize(CellAutomaton automaton, int quantity, int radius);
}
