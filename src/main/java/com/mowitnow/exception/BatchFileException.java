package com.mowitnow.exception;

public class BatchFileException extends RuntimeException {
    public BatchFileException(String message) {
        super(message);
    }
    public BatchFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
