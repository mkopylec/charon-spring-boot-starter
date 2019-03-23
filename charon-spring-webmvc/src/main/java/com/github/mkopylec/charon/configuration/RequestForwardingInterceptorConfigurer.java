package com.github.mkopylec.charon.configuration;

import java.util.function.Consumer;

import com.github.mkopylec.charon.core.interceptors.RequestForwardingInterceptor;

public abstract class RequestForwardingInterceptorConfigurer<I extends RequestForwardingInterceptor> {

    private I requestForwardingInterceptor;

    protected RequestForwardingInterceptorConfigurer(I requestForwardingInterceptor) {
        this.requestForwardingInterceptor = requestForwardingInterceptor;
    }

    protected void configure(Consumer<I> interceptorConfigurer) {
        interceptorConfigurer.accept(requestForwardingInterceptor);
    }

    I getRequestForwardingInterceptor() {
        return requestForwardingInterceptor;
    }
}
