package com.github.mkopylec.charon.forwarding.interceptors.metrics;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;
import io.micrometer.core.instrument.MeterRegistry;

public class RateMeterConfigurer extends RequestForwardingInterceptorConfigurer<RateMeter> {

    private RateMeterConfigurer() {
        super(new RateMeter());
    }

    public static RateMeterConfigurer rateMeter() {
        return new RateMeterConfigurer();
    }

    public RateMeterConfigurer meterRegistry(MeterRegistry meterRegistry) {
        configuredObject.setMeterRegistry(meterRegistry);
        return this;
    }
}
