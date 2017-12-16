package com.dudududi.grainexpansion.model.cells;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import org.apache.commons.csv.CSVRecord;

import java.util.*;

/**
 * Created by dudek on 4/21/16.
 */
public class Cell {
    public static final String[] CSV_HEADERS = new String[]{"x", "y", "color", "state"};
    public static final int MIN_ENERGY = 0;
    public static final int MAX_ENERGY = 100;

    private IntegerProperty energyProperty;
    private CoordinatePair position;
    private State state;

    Cell(CoordinatePair position) {
        this.position = position;
        this.energyProperty = new SimpleIntegerProperty(0);
        setState(State.DEAD_STATE);
    }

    public IntegerProperty energyProperty() {
        return energyProperty;
    }

    public CoordinatePair getPosition() {
        return position;
    }

    public void setState(State state) {
        this.state = state;
        if (state.phase == Phase.RECRYSTALLIZED) {
            setEnergy(-1);
        }
    }

    void reset() {
        setState(State.DEAD_STATE);
    }

    public void setEnergy(int energy) {
        energyProperty.setValue(energy);
    }

    public int getEnergy() {
        return energyProperty.get();
    }

    public boolean isAlive() {
        return state.phase == Phase.ALIVE;
    }

    public boolean isDead() {
        return state.phase == Phase.DEAD;
    }

    public boolean isExcluded() {
        return state.phase == Phase.INCLUSION ||
                state.phase == Phase.SUB_STRUCTURE ||
                state.phase == Phase.BOUNDARY;
    }

    public boolean isRecrystallized() {
        return state.phase == Phase.RECRYSTALLIZED;
    }

    public State getState() {
        return state;
    }

    public Snapshot recordSnapshot() {
        return new Snapshot(position, state.color, energyProperty.get());
    }

    Iterable<String> toCSVRecord() {
        return Arrays.asList(
                String.valueOf(position.x),     // x
                String.valueOf(position.y),     // y
                state.color.toString(),         // color
                String.valueOf(state.phase)     // state
        );
    }

    static Cell fromCSVRecord(CSVRecord record) {
        CoordinatePair position = new CoordinatePair(Integer.parseInt(record.get("x")), Integer.parseInt(record.get("y")));
        Cell cell = new Cell(position);
        Color color = Color.valueOf(record.get("color"));
        Phase phase = Phase.byID(Integer.parseInt(record.get("state")));
        cell.setState(new State(phase, color));
        return cell;
    }

    public enum Phase {
        DEAD(0),
        ALIVE(1),
        INCLUSION(2),
        SUB_STRUCTURE(3),
        BOUNDARY(4),
        RECRYSTALLIZED(5);

        private final int id;

        Phase(int id) {
            this.id = id;
        }

        static Phase byID(int id) {
            switch (id) {
                case 1:
                    return ALIVE;
                case 2:
                    return INCLUSION;
                default:
                    return DEAD;
            }
        }
    }

    public static class State {
        private static final Random random = new Random();
        private final Phase phase;
        private final Color color;

        static final State DEAD_STATE = new State(Phase.DEAD, Color.WHITE);

        public State(Phase phase, Color color) {
            this.phase = phase;
            this.color = color;
        }

        public static State createAliveState() {
            Color randomColor = new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1);
            return new State(Phase.ALIVE, randomColor);
        }

        public static State createRecrystallizedState() {
            Color randomColor = Color.gray(random.nextDouble());
            return new State(Phase.RECRYSTALLIZED, randomColor);
        }

        public Color getColor() {
            return color;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof State)) return false;

            State stateObj = (State) obj;
            return color.equals(stateObj.color) && phase.id == stateObj.phase.id;
        }

        @Override
        public int hashCode() {
            // as we want to hold each instance in GrainsWarehouse' HashMap separately,
            // we are adding super.hashCode() to the result
            return super.hashCode() + Objects.hash(color, phase.id);
        }
    }

    public static class Snapshot {
        private final CoordinatePair position;
        private final Color color;
        private final int energy;

        private Snapshot(CoordinatePair position, Color color, int energy) {
            this.position = position;
            this.color = color;
            this.energy = energy;
        }

        public CoordinatePair getPosition() {
            return position;
        }

        public Color getColor() {
            return color;
        }

        public int getEnergy() {
            return energy;
        }
    }
}
