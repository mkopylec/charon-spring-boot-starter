package com.github.mkopylec.charon.core.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class RequestData {

    protected final HttpMethod method;
    protected final String uri;
    protected final HttpHeaders headers;
    protected final byte[] body;

    public RequestData(HttpMethod method, String uri, HttpHeaders headers, byte[] body) {
        this.method = method;
        this.uri = uri;
        this.headers = headers;
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
