package com.github.mkopylec.charon.interceptors;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;

import static org.apache.commons.io.IOUtils.toByteArray;

public class HttpResponse implements ClientHttpResponse {

    private HttpStatus status;
    private HttpHeaders headers;
    private byte[] body;
    private Runnable closeHandler;

    HttpResponse(ClientHttpResponse response) throws IOException {
        status = response.getStatusCode();
        headers = new HttpHeaders();
        headers.putAll(response.getHeaders());
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

    @Override
    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
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
