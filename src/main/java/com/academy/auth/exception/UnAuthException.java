package com.academy.auth.exception;

public class UnAuthException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnAuthException(String message) {
        super(message);
    }

    public UnAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnAuthException(Throwable cause) {
        super(cause);
    }
}
