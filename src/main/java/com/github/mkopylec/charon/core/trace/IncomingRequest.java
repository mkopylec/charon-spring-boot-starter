package com.github.mkopylec.charon.core.trace;

import org.springframework.http.HttpMethod;

public class IncomingRequest extends HttpEntity {

    protected HttpMethod method;
    protected String uri;

    public HttpMethod getMethod() {
        return method;
    }

    @SuppressWarnings("unchecked")
    public <T extends IncomingRequest> T setMethod(HttpMethod method) {
        this.method = method;
        return (T) this;
    }

    public String getUri() {
        return uri;
    }

    @SuppressWarnings("unchecked")
    public <T extends IncomingRequest> T setUri(String uri) {
        this.uri = uri;
        return (T) this;
    }
}
