package com.mowitnow.model;

/**
 * Bean representing the mower
 */
public class Mower {
    private int x;
    private int y;
    private final char orientation;
    private final String instructions;

    public Mower(int x, int y, char orientation, String instructions) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.instructions = instructions;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getOrientation() {
        return orientation;
    }

    public String getInstructions() {
        return instructions;
    }

    /**
     * Create a new mower with the given position
     *
     * @param newX the new x coordinate
     * @param newY the new y coordinate
     * @return a new mower with the given position
     */
    public Mower withPosition(int newX, int newY) {
        return new Mower(newX, newY, orientation, instructions);
    }

    /**
     * Create a new mower with the given orientation
     *
     * @param newOrientation the new orientation
     * @return a new mower with the given orientation
     */
    public Mower withOrientation(char newOrientation) {
        return new Mower(x, y, newOrientation, instructions);
    }
}
