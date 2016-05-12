package com.dudududi.grainexpansion.controller.randomizable;

import com.dudududi.grainexpansion.controller.Randomizable;
import com.dudududi.grainexpansion.model.Cell;
import com.dudududi.grainexpansion.model.CellAutomaton;

import java.util.Random;

/**
 * Created by dudek on 5/7/16.
 */
public class RadiusRandomize implements Randomizable {
    private Random random;

    public RadiusRandomize(){
        random = new Random();
    }
    @Override
    public void randomize(CellAutomaton automaton, int quantity, int radius) {
        Cell[][] cells = automaton.getCells();
        for (int k = 0; k < quantity; k++){
            int xCenter = random.nextInt(automaton.getWidth());
            int yCenter = random.nextInt(automaton.getHeight());
            int leftBound = xCenter-radius < 0 ? 0 : xCenter - radius;
            int rightBound = xCenter+radius > automaton.getWidth() ? automaton.getWidth() : xCenter+radius;
            int downBound = yCenter-radius < 0 ? 0 : yCenter - radius;
            int upBound = yCenter+radius > automaton.getHeight() ? automaton.getWidth() : yCenter+radius;
            boolean foundedInRadius;
            int attempts = 0;
            do {
                foundedInRadius = false;
                for (int i = leftBound; i < rightBound; i++) {
                    for (int j = downBound; j < upBound; j++) {
                        if (i != xCenter && j != yCenter && cells[i][j].isAlive()) {
                            foundedInRadius = true;
                            attempts++;
                            break;
                        }
                    }
                    if (foundedInRadius) break;
                }
            } while (foundedInRadius && attempts < 1000);
            if (!foundedInRadius) {
                cells[xCenter][yCenter].setAliveWithRandomColor();
            }
        }
    }

    @Override
    public String toString(){
        return "Losowe z promieniem";
    }
}
