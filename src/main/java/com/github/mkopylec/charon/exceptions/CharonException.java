package com.github.mkopylec.charon.exceptions;

public class CharonException extends RuntimeException {

    public CharonException(String message) {
        super(message);
    }

    public CharonException(String message, Throwable cause) {
        super(message, cause);
    }
}
