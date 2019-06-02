package com.github.mkopylec.charon.forwarding;

import java.util.function.Supplier;

public class RequestForwardingException extends RuntimeException {

    private RequestForwardingException(String message) {
        super(message);
    }

    private RequestForwardingException(String message, Throwable cause) {
        super(message, cause);
    }

    public static void requestForwardingErrorIf(boolean condition, Supplier<String> errorMessage) {
        if (condition) {
            throw new RequestForwardingException(errorMessage.get());
        }
    }

    public static RequestForwardingException requestForwardingError(String errorMessage) {
        return new RequestForwardingException(errorMessage);
    }

    public static RequestForwardingException requestForwardingError(String errorMessage, Throwable cause) {
        return new RequestForwardingException(errorMessage, cause);
    }
}
