package com.github.mkopylec.charon.core.trace;

import org.springframework.http.HttpHeaders;

import static java.nio.charset.Charset.forName;

public abstract class HttpEntity {

    protected byte[] body;
    protected HttpHeaders headers;

    public String getBody() {
        return convertBodyToString(body);
    }

    @SuppressWarnings("unchecked")
    public <T extends HttpEntity> T setBody(byte[] body) {
        this.body = body;
        return (T) this;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    @SuppressWarnings("unchecked")
    public <T extends HttpEntity> T setHeaders(HttpHeaders headers) {
        this.headers = headers;
        return (T) this;
    }

    protected String convertBodyToString(byte[] body) {
        if (body == null) {
            return null;
        }
        return new String(body, forName("UTF-8"));
    }
}
