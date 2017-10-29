package com.dudududi.grainexpansion.model.cells;

import com.dudududi.grainexpansion.model.CoordinatePair;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import org.apache.commons.csv.CSVRecord;

import java.util.*;

/**
 * Created by dudek on 4/21/16.
 */
public class Cell {
    public static final String[] CSV_HEADERS = new String[]{"x", "y", "color", "state"};

    private ObjectProperty<Color> colorProperty;
    private CoordinatePair position;
    private State state;

    public Cell(CoordinatePair position) {
        this.position = position;
        this.colorProperty = new SimpleObjectProperty<>(Color.WHITE);
        setState(new State(Phase.DEAD, Color.WHITE));
    }

    public ObjectProperty<Color> colorProperty() {
        return colorProperty;
    }

    public CoordinatePair getPosition() {
        return position;
    }

    public void setState(State state) {
        this.state = state;
        colorProperty.setValue(state.color);
    }

    public void reset() {
        setState(new State(Phase.DEAD, Color.WHITE));
    }


    public boolean isAlive() {
        return state.phase == Phase.ALIVE;
    }

    public boolean isInclusion() {
        return state.phase == Phase.INCLUSION;
    }

    public State getState() {
        return state;
    }

    public Iterable<String> toCSVRecord() {
        return Arrays.asList(
                String.valueOf(position.x),     // x
                String.valueOf(position.y),     // y
                colorProperty.get().toString(), // color
                String.valueOf(state.phase)     // state
        );
    }

    public static Cell fromCSVRecord(CSVRecord record) {
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
        INCLUSION(2);

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

    public static class State{
        private final Phase phase;
        private final Color color;

        public State(Phase phase, Color color) {
            this.phase = phase;
            this.color = color;
        }

        public static State copyState(State state) {
            return new State(state.phase, state.color);
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
            return Objects.hash(color, phase.id);
        }
    }
}
