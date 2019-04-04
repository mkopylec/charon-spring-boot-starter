package com.github.mkopylec.charon.interceptors;

public class RequestForwardingException extends RuntimeException {

    private RequestForwardingException(String message) {
        super(message);
    }

    private RequestForwardingException(String message, Throwable cause) {
        super(message, cause);
    }

    public static void requestForwardingErrorIf(boolean condition, String errorMessage) {
        if (condition) {
            throw new RequestForwardingException(errorMessage);
        }
    }

    public static RequestForwardingException requestForwardingError(String errorMessage, Throwable cause) {
        return new RequestForwardingException(errorMessage, cause);
    }
}
