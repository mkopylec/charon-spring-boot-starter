package com.github.mkopylec.charon.forwarding;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class HttpResponse {

    private HttpStatus status;
    private HttpHeaders headers;
    private byte[] body;

    HttpResponse(HttpStatus status, HttpHeaders headers, byte[] body) {
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
}
