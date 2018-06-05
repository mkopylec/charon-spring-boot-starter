package com.github.mkopylec.charon.core.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import static com.github.mkopylec.charon.core.utils.BodyConverter.convertBodyToString;

public class UnmodifiableRequestData {

    protected HttpMethod method;
    protected String uri;
    protected HttpHeaders headers;
    protected byte[] body;

    public UnmodifiableRequestData(RequestData requestData) {
        this(requestData.getMethod(), requestData.getUri(), requestData.getHeaders(), requestData.getBody());
    }

    public UnmodifiableRequestData(HttpMethod method, String uri, HttpHeaders headers, byte[] body) {
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

    public String getBodyAsString() {
        return convertBodyToString(body);
    }
}
