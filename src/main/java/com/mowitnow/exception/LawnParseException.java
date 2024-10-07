package com.mowitnow.exception;

public class LawnParseException extends RuntimeException {
    public LawnParseException(String message) {
        super(message);
    }

    public LawnParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
