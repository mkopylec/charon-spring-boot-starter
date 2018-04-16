package com.github.mkopylec.charon.core.http;

import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class RequestData {

    protected HttpMethod method;
    protected String uri;
    protected HttpHeaders headers;
    protected InputStream body;

    public RequestData(HttpMethod method, String uri, HttpHeaders headers, InputStream body) {
        this.method = method;
        this.uri = uri;
        this.headers = headers;
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public InputStream getBody() {
        return body;
    }

    public void setBody(InputStream body) {
        this.body = body;
    }
}
