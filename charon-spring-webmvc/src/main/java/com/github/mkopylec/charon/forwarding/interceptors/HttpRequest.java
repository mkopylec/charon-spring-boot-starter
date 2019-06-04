package com.github.mkopylec.charon.forwarding.interceptors;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;

public class HttpRequest implements org.springframework.http.HttpRequest {

    private URI uri;
    private HttpMethod method;
    private HttpHeaders headers;
    private byte[] body;

    HttpRequest(org.springframework.http.HttpRequest request, byte[] body) {
        uri = request.getURI();
        method = request.getMethod();
        headers = request.getHeaders();
        this.body = body;
    }

    @Override
    public URI getURI() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
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
}
