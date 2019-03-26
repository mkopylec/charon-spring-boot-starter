package com.github.mkopylec.charon.interceptors.resilience;

import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptorConfigurer;

public abstract class ResilienceHandlerConfigurer<H extends ResilienceHandler<?>, C extends ResilienceHandlerConfigurer<H, C>> extends RequestForwardingInterceptorConfigurer<H> {

    protected ResilienceHandlerConfigurer(H configuredObject) {
        super(configuredObject);
    }

    @SuppressWarnings("unchecked")
    public C enabled(boolean enabled) {
        configuredObject.setEnabled(enabled);
        return (C) this;
    }

    @SuppressWarnings("unchecked")
    public C measured(boolean measured) {
        configuredObject.setMeasured(measured);
        return (C) this;
    }
}
