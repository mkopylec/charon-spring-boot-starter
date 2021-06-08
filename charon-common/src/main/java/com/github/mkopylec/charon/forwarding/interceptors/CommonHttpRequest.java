package com.github.mkopylec.charon.forwarding.interceptors;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public interface CommonHttpRequest {

    /**
     * Return the HTTP method.
     */
    HttpMethod method();

    /**
     * Return the request URI.
     */
    URI url();

    /**
     * Return the headers of this request.
     */
    HttpHeaders headers();
}
