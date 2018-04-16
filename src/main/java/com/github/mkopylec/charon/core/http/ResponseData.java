package com.github.mkopylec.charon.core.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class ResponseData {

    protected HttpStatus status;
    protected HttpHeaders headers;

    public ResponseData(HttpStatus status, HttpHeaders headers) {
        this.status = status;
        this.headers = headers;
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
}
