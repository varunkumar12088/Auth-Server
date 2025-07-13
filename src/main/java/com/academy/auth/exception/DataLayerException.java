package com.academy.auth.exception;

public class DataLayerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataLayerException(String message) {
        super(message);
    }

    public DataLayerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataLayerException(Throwable cause) {
        super(cause);
    }
}
