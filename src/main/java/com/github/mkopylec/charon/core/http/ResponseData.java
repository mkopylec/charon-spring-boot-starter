package com.github.mkopylec.charon.core.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static com.github.mkopylec.charon.core.utils.BodyConverter.convertBodyToString;
import static com.github.mkopylec.charon.core.utils.BodyConverter.convertStringToBody;

public class ResponseData {

    protected HttpStatus status;
    protected HttpHeaders headers;
    protected byte[] body;
    protected String uri;
    protected HttpMethod method;

    public ResponseData(HttpStatus status, HttpHeaders headers, byte[] body, String uri, HttpMethod method;) {
        this.status = status;
        this.headers = new HttpHeaders();
        this.headers.putAll(headers);
        this.body = body;
        this.uri = uri;
        this.method = method;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
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

    public String getBodyAsString() {
        return convertBodyToString(body);
    }

    public void setBody(String body) {
        this.body = convertStringToBody(body);
    }

    public String getUri() {
        return uri;
    }

    public HttpMethod getMethod() {
        return method;
    }
}
