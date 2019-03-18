package com.github.mkopylec.charon.configuration;

import java.util.function.Consumer;

import com.github.mkopylec.charon.core.interceptors.ForwardingStartInterceptor;

public abstract class ForwardingStartInterceptorConfigurer<I extends ForwardingStartInterceptor> {

    private I forwardingStartInterceptor;

    protected ForwardingStartInterceptorConfigurer(I forwardingStartInterceptor) {
        this.forwardingStartInterceptor = forwardingStartInterceptor;
    }

    protected void configure(Consumer<I> interceptorConfigurer) {
        interceptorConfigurer.accept(forwardingStartInterceptor);
    }

    I getForwardingStartInterceptor() {
        return forwardingStartInterceptor;
    }
}
