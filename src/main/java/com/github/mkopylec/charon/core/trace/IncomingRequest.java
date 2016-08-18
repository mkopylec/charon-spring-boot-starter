package com.github.mkopylec.charon.core.trace;

import org.springframework.http.HttpMethod;

public class IncomingRequest extends HttpEntity {

    protected HttpMethod method;
    protected String uri;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
