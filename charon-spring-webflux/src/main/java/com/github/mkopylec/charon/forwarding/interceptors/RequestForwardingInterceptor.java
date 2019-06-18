package com.github.mkopylec.charon.forwarding.interceptors;

import com.github.mkopylec.charon.configuration.Valid;
import reactor.core.publisher.Mono;

public interface RequestForwardingInterceptor extends Valid, Comparable<RequestForwardingInterceptor> {

    Mono<HttpResponse> forward(HttpRequest request, HttpRequestExecution execution);

    RequestForwardingInterceptorType getType();

    @Override
    default int compareTo(RequestForwardingInterceptor requestForwardingInterceptor) {
        return getType().compareTo(requestForwardingInterceptor.getType());
    }
}
