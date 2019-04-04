package com.github.mkopylec.charon.interceptors;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import static java.net.URI.create;
import static org.springframework.http.HttpHeaders.readOnlyHttpHeaders;

public class HttpRequest implements org.springframework.http.HttpRequest {

    private URI uri;
    private URI originalUri;
    private HttpMethod method;
    private HttpMethod originalMethod;
    private HttpHeaders headers;
    private HttpHeaders originalHeaders;
    private byte[] body;

    HttpRequest(org.springframework.http.HttpRequest request, byte[] body) {
        uri = request.getURI();
        originalUri = create(uri.toString());
        method = request.getMethod();
        method = request.getMethod();
        headers = new HttpHeaders();
        headers.putAll(request.getHeaders());
        originalHeaders = readOnlyHttpHeaders(request.getHeaders());
        this.body = body;
    }

    @Override
    public URI getURI() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public URI getOriginalUri() {
        return originalUri;
    }

    @Override
    public String getMethodValue() {
        return method.name();
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public HttpMethod getOriginalMethod() {
        return originalMethod;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public HttpHeaders getOriginalHeaders() {
        return originalHeaders;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
