package com.github.mkopylec.charon.core.trace;

import org.springframework.http.HttpHeaders;

import static java.nio.charset.Charset.forName;

public abstract class HttpEntity {

    protected HttpHeaders headers;

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    protected String convertBodyToString(byte[] body) {
        if (body == null) {
            return null;
        }
        return new String(body, forName("UTF-8"));
    }
}
