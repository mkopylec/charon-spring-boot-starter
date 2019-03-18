package com.github.mkopylec.charon.core;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class HttpRequest {

    private URI uri;
    private HttpMethod method;
    private HttpHeaders headers;
    private byte[] body;
}
