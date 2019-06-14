package com.github.mkopylec.charon.forwarding.interceptors.metrics;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;
import io.micrometer.core.instrument.MeterRegistry;

public class LatencyMeterConfigurer extends RequestForwardingInterceptorConfigurer<LatencyMeter> {

    private LatencyMeterConfigurer() {
        super(new LatencyMeter());
    }

    public static LatencyMeterConfigurer latencyMeter() {
        return new LatencyMeterConfigurer();
    }

    public LatencyMeterConfigurer meterRegistry(MeterRegistry meterRegistry) {
        configuredObject.setMeterRegistry(meterRegistry);
        return this;
    }
}
