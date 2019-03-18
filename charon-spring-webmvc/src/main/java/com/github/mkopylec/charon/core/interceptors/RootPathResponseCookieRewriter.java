package com.github.mkopylec.charon.core.interceptors;

import com.github.mkopylec.charon.core.HttpRequest;
import com.github.mkopylec.charon.core.HttpResponse;

public class RootPathResponseCookieRewriter implements ForwardingCompleteInterceptor {

    protected RootPathResponseCookieRewriter() {
    }

    @Override
    public void onSuccess(String traceId, HttpRequest outgoingRequest, HttpResponse outgoingResponse) {
        // TODO Implement
    }

    @Override
    public void onError(String traceId, HttpRequest outgoingRequest, HttpResponse outgoingResponse, Exception exception) {
        // TODO Implement
    }
}
