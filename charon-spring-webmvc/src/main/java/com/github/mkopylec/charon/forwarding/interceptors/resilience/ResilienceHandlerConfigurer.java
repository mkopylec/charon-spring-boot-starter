package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;
import io.micrometer.core.instrument.MeterRegistry;

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
    public C meterRegistry(MeterRegistry meterRegistry) {
        configuredObject.setMeterRegistry(meterRegistry);
        return (C) this;
    }
}
