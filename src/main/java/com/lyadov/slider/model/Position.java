package com.lyadov.slider.model;

/**
 * Representation of the slideshow progress position.
 */
public class Position {
    public static final Position EMPTY = new Position(0, 0);

    private final int current;
    private final int total;

    public Position(int current, int total) {
        this.current = current;
        this.total = total;
    }

    public int getCurrent() {
        return current;
    }

    public int getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return current + " / " + total;
    }
}
