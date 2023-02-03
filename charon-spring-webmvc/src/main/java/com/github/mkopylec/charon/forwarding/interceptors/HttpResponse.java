package com.github.mkopylec.charon.forwarding.interceptors;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;

import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;
import static org.apache.commons.io.IOUtils.toByteArray;

public class HttpResponse implements ClientHttpResponse {

    private HttpStatusCode status;
    private HttpHeaders headers;
    private byte[] body;

    public HttpResponse(HttpStatusCode status) {
        this.status = status;
        headers = new HttpHeaders();
        body = new byte[]{};
    }

    HttpResponse(ClientHttpResponse response) throws IOException {
        status = response.getStatusCode();
        headers = response.getHeaders();
        body = toByteArray(response.getBody());
        response.close();
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return status;
    }

    @Override
    public int getRawStatusCode() {
        return status.value();
    }

    @Override
    public String getStatusText() {
        return status.toString();
    }

    public void setStatusCode(HttpStatusCode status) {
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
        HttpHeaders rewrittenHeaders = copyHeaders(headers);
        rewrittenHeaders.setContentLength(body.length);
        setHeaders(rewrittenHeaders);
    }

    @Override
    public void close() {
    }
}
