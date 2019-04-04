package com.github.mkopylec.charon.interceptors;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import static org.apache.commons.io.IOUtils.toByteArray;
import static org.springframework.http.HttpHeaders.readOnlyHttpHeaders;

public class HttpResponse implements ClientHttpResponse {

    private HttpStatus status;
    private HttpStatus originalStatus;
    private HttpHeaders headers;
    private HttpHeaders originalHeaders;
    private byte[] body;
    private Runnable closeHandler;

    public HttpResponse() {
    }

    HttpResponse(ClientHttpResponse response) throws IOException {
        status = response.getStatusCode();
        originalStatus = response.getStatusCode();
        headers = new HttpHeaders();
        headers.putAll(response.getHeaders());
        originalHeaders = readOnlyHttpHeaders(response.getHeaders());
        body = toByteArray(response.getBody());
        closeHandler = response::close;
    }

    @Override
    public HttpStatus getStatusCode() {
        return status;
    }

    @Override
    public int getRawStatusCode() {
        return status.value();
    }

    @Override
    public String getStatusText() {
        return status.getReasonPhrase();
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getOriginalStatus() {
        return originalStatus;
    }

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public HttpHeaders getOriginalHeaders() {
        return originalHeaders;
    }

    @Override
    public InputStream getBody() {
        return new ByteArrayInputStream(body);
    }

    public byte[] getBodyAsBytes() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public void close() {
        closeHandler.run();
    }
}
