package com.dudududi.grainexpansion.model.rules;

import com.dudududi.grainexpansion.model.cells.Cell;
import com.dudududi.grainexpansion.model.cells.CellState;
import javafx.scene.paint.Color;

import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by dudek on 4/28/16.
 */
@SuppressWarnings("all") //TODO: to be removed and refactored
public class SimpleMoore implements Rule {
    private int probability;
    private Random random;
    public SimpleMoore() {
        this.probability = 100;
        this.random = new Random();
    }
    @Override
    public CellState shouldCellBeAlive(Cell cell) {
        if (!cell.getState().getType().equals(CellState.Type.DEAD)) return null; // if not dead...
        Map<Color, Long> counts =  cell.getNeighbourhood().stream()
                .filter(c -> c.isAlive())
                .collect(Collectors.groupingBy(Cell::getColor, Collectors.counting()));
        if (counts.size() == 0) return null;
        Color color = counts.entrySet().stream()
                .max((e1, e2) -> e1.getValue().compareTo(e2.getValue()))
                .get()
                .getKey();
        return willBeAlive() ? new CellState(color, CellState.Type.ALIVE) : null;
    }

    @Override
    public boolean isCellOnBoundary(Cell cell) {
        return cell.getNeighbourhood().stream()
                .anyMatch(c -> !(c.getColor().equals(cell.getColor()) && cell.isAlive()));
    }

    private boolean willBeAlive() {
        if (probability == 100) return true;

        int rand = random.nextInt(100);
        return rand <= probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    @Override
    public String toString() {
        return "Simple";
    }
}
