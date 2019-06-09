package com.github.mkopylec.charon.forwarding.interceptors;

import com.github.mkopylec.charon.configuration.Configurer;

public abstract class RequestForwardingInterceptorConfigurer<I extends RequestForwardingInterceptor> extends Configurer<I> {

    protected RequestForwardingInterceptorConfigurer(I configuredObject) {
        super(configuredObject);
    }
}
