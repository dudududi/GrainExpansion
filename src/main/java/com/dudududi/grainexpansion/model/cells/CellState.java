package com.dudududi.grainexpansion.model.cells;

import javafx.scene.paint.Color;


/**
 * Created by dudek on 10/16/17.
 */
public class CellState {
    private final Type type;
    private final Color color;

    public CellState(Color color, Type type) {
        this.color = color;
        this.type = type;
    }

    Color getColor() {
        return color;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        DEAD(0),
        ALIVE(1),
        INCLUSION(2),
        SUBSTRUCTURE(3),
        DUAL_PHASE(4),
        BOUNDARY(5);

        private final int stateId;

        Type(int id) {
            stateId = id;
        }

        int getStateId() {
            return stateId;
        }

        public static Type byID(int id) {
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
}
