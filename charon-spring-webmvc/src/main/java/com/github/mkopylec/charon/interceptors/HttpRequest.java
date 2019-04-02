package com.github.mkopylec.charon.interceptors;

import java.net.URI;

import com.github.mkopylec.charon.forwarding.CustomConfiguration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

public class HttpRequest implements org.springframework.http.HttpRequest {

    private URI uri;
    private HttpMethod method;
    private HttpHeaders headers;
    private byte[] body;
    private String name;
    private CustomConfiguration customConfiguration;

    HttpRequest(org.springframework.http.HttpRequest request, byte[] body, String name, CustomConfiguration customConfiguration) {
        uri = request.getURI();
        method = request.getMethod();
        headers = new HttpHeaders();
        headers.putAll(request.getHeaders());
        this.body = body;
        this.name = name;
        this.customConfiguration = customConfiguration;
    }

    @Override
    public URI getURI() {
        return uri;
    }

    public void setURI(URI uri) {
        this.uri = uri;
    }

    @Override
    public String getMethodValue() {
        return method.name();
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
    }

    public String getName() {
        return name;
    }

    public <P> P getCustomProperty(String name) {
        return customConfiguration.getProperty(name);
    }
}
