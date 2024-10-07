package com.mowitnow.exception;

public class MowerParseException extends Exception {
    public MowerParseException(String message) {
        super(message);
    }

    public MowerParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
