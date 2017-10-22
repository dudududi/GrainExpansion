package com.dudududi.grainexpansion.model;

import java.util.Objects;

/**
 * Created by dudek on 4/27/16.
 */
public class CoordinatePair {
    public int x, y;

    public CoordinatePair(int dx, int dy) {
        this.x = dx;
        this.y = dy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof CoordinatePair)) return false;

        CoordinatePair pair = (CoordinatePair) obj;

        return x == pair.x && y == pair.y;

    }
}
