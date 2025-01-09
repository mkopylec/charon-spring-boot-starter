package com.github.mkopylec.charon.forwarding.interceptors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.net.URI;
import java.util.Map;

import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;

public class HttpRequest implements org.springframework.http.HttpRequest {

    private URI uri;
    private HttpMethod method;
    private HttpHeaders headers;
    private byte[] body;
    private final Map<String, Object> attributes;

    HttpRequest(org.springframework.http.HttpRequest request, byte[] body) {
        uri = request.getURI();
        method = request.getMethod();
        headers = request.getHeaders();
        this.body = body;
        attributes = request.getAttributes();
    }

    @Override
    public URI getURI() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    @Override
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
        HttpHeaders rewrittenHeaders = copyHeaders(headers);
        rewrittenHeaders.setContentLength(body.length);
        setHeaders(rewrittenHeaders);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
