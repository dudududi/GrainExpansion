package com.dudududi.grainexpansion.model;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by dudek on 4/27/16.
 */
public abstract class Structure {
    private List<CoordinatePair> relativeCoordinates;
    private String name;
    private Random random;

    public Structure() {
        relativeCoordinates = getStructureDefinition();
    }

    public Structure(String name) {
        relativeCoordinates = getStructureDefinition();
        this.name = name;
        random = new Random();
    }

    @Override
    public String toString() {
        return name;
    }

    public void drawStructureOnAutomaton(CellAutomaton automaton) {
        CoordinatePair startPoint = new CoordinatePair(random.nextInt(automaton.getWidth()), random.nextInt(automaton.getHeight()));
        Cell[][] cells = automaton.getCells();
        List<CoordinatePair> toDraw = relativeCoordinates.stream()
                .filter(coordinatePair -> startPoint.x + coordinatePair.x >= 0 &&
                        startPoint.x + coordinatePair.x < automaton.getWidth() &&
                        startPoint.y + coordinatePair.y >= 0 &&
                        startPoint.y + coordinatePair.y < automaton.getHeight())
                .collect(Collectors.toList());
        toDraw.stream().forEach(pair -> cells[startPoint.x + pair.x][startPoint.y + pair.y].setAlive(true));
    }

    protected abstract List<CoordinatePair> getStructureDefinition();
}
