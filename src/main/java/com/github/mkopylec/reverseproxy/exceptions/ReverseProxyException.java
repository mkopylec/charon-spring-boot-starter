package com.github.mkopylec.reverseproxy.exceptions;

public class ReverseProxyException extends RuntimeException {

	public ReverseProxyException(String message) {
		super(message);
	}

	public ReverseProxyException(String message, Throwable cause) {
		super(message, cause);
	}
}
