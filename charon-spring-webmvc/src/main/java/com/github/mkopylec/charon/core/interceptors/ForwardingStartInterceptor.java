package com.github.mkopylec.charon.core.interceptors;

import com.github.mkopylec.charon.core.HttpRequest;

// TODO X forwarded headers interceptor
public interface ForwardingStartInterceptor {

    void onStart(String traceId, HttpRequest outgoingRequest);
}
