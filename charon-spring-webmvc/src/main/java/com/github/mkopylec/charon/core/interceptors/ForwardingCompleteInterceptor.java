package com.github.mkopylec.charon.core.interceptors;

import com.github.mkopylec.charon.core.HttpRequest;
import com.github.mkopylec.charon.core.HttpResponse;

public interface ForwardingCompleteInterceptor {

    void onSuccess(String traceId, HttpRequest outgoingRequest, HttpResponse outgoingResponse);

    void onError(String traceId, HttpRequest outgoingRequest, HttpResponse outgoingResponse, Exception exception);
}
