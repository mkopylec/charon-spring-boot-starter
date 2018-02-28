package com.github.mkopylec.charon.core.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static com.github.mkopylec.charon.core.utils.BodyConverter.convertBodyToString;
import static com.github.mkopylec.charon.core.utils.BodyConverter.convertStringToBody;

public class ResponseData {

    protected HttpStatus status;
    protected HttpHeaders headers;
    protected byte[] body;

    public ResponseData(HttpStatus status, HttpHeaders headers, byte[] body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
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
}
