package com.github.mkopylec.charon.configuration;

import java.util.function.Consumer;

import com.github.mkopylec.charon.core.interceptors.ForwardingCompleteInterceptor;

public abstract class ForwardingCompleteInterceptorConfigurer<I extends ForwardingCompleteInterceptor> {

    private I forwardingCompleteInterceptor;

    protected ForwardingCompleteInterceptorConfigurer(I forwardingCompleteInterceptor) {
        this.forwardingCompleteInterceptor = forwardingCompleteInterceptor;
    }

    protected void configure(Consumer<I> interceptorConfigurer) {
        interceptorConfigurer.accept(forwardingCompleteInterceptor);
    }

    I getForwardingCompleteInterceptor() {
        return forwardingCompleteInterceptor;
    }
}
