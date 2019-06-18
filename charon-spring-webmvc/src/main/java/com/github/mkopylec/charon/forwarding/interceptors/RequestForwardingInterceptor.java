package com.github.mkopylec.charon.forwarding.interceptors;

import com.github.mkopylec.charon.configuration.Valid;

public interface RequestForwardingInterceptor extends Valid, Comparable<RequestForwardingInterceptor> {

    HttpResponse forward(HttpRequest request, HttpRequestExecution execution);

    RequestForwardingInterceptorType getType();

    @Override
    default int compareTo(RequestForwardingInterceptor requestForwardingInterceptor) {
        return getType().compareTo(requestForwardingInterceptor.getType());
    }
}
