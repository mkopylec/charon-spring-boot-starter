package com.github.mkopylec.charon.core.trace;

import org.springframework.http.HttpStatus;

public class ReceivedResponse extends HttpEntity {

    protected HttpStatus status;

    public HttpStatus getStatus() {
        return status;
    }

    @SuppressWarnings("unchecked")
    public <T extends ReceivedResponse> T setStatus(HttpStatus status) {
        this.status = status;
        return (T) this;
    }
}
