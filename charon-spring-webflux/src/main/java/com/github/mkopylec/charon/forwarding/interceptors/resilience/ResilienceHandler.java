package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;

public abstract class ResilienceHandler<R> extends BasicResilienceHandler<R> implements RequestForwardingInterceptor {

    ResilienceHandler(R registry) {
        super(registry);
    }
}
