package com.github.mkopylec.reverseproxy;

public class ReverseProxyException extends RuntimeException {

	public ReverseProxyException(String message) {
		super(message);
	}

	public ReverseProxyException(String message, Throwable cause) {
		super(message, cause);
	}
}
