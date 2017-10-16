package com.dudududi.grainexpansion.controller.randomizable;

import com.dudududi.grainexpansion.controller.Randomizable;
import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.CellAutomaton;

/**
 * Created by dudek on 5/7/16.
 */
public class StaticRandomize implements Randomizable {
    @Override
    public void randomize(CellAutomaton automaton, int quantity, int radius) {
        Cell[][] cells = automaton.getCells();
        int numberInRow = (int)Math.sqrt(quantity);
        int distanceInWidth = automaton.getWidth()/numberInRow;
        int distanceInHeight = automaton.getHeight()/numberInRow;
        for (int i = 0; i < automaton.getWidth(); i+=distanceInWidth){
            for (int j = 0; j < automaton.getHeight(); j+=distanceInHeight){
                cells[i][j].setAliveWithRandomColor();
            }
        }
    }

    @Override
    public String toString(){
        return "Równomierne";
    }
}
