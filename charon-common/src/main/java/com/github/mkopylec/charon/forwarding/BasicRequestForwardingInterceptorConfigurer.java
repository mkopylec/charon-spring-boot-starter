package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.Configurer;

public abstract class BasicRequestForwardingInterceptorConfigurer<I extends BasicRequestForwardingInterceptor> extends Configurer<I> {

    protected BasicRequestForwardingInterceptorConfigurer(I configuredObject) {
        super(configuredObject);
    }
}
