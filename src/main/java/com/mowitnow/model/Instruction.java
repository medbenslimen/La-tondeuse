package com.mowitnow.model;

public enum Instruction {
    MOVE('A'),
    TURN_LEFT('G'),
    TURN_RIGHT('D');

    private final char code;

    Instruction(char code) {
        this.code = code;
    }

    public static Instruction fromCode(char code) {
        return switch (code) {
            case 'A' -> MOVE;
            case 'G' -> TURN_LEFT;
            case 'D' -> TURN_RIGHT;
            default -> throw new IllegalArgumentException("Invalid instruction code: " + code);
        };
    }
}
