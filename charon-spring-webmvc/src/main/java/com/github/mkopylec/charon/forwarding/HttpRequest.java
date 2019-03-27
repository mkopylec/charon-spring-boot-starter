package com.github.mkopylec.charon.forwarding;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class HttpRequest {

    private URI uri;
    private HttpMethod method;
    private HttpHeaders headers;
    private byte[] body;

    HttpRequest(URI uri, HttpMethod method, HttpHeaders headers, byte[] body) {
        this.uri = uri;
        this.method = method;
        this.headers = headers;
        this.body = body;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
