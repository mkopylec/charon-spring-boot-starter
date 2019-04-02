package com.github.mkopylec.charon.interceptors.latency;

import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptorConfigurer;
import io.micrometer.core.instrument.MeterRegistry;

public class LatencyMeterConfigurer extends RequestForwardingInterceptorConfigurer<LatencyMeter> {

    private LatencyMeterConfigurer(MeterRegistry meterRegistry) {
        super(new LatencyMeter(meterRegistry));
    }

    public static LatencyMeterConfigurer latencyMeter(MeterRegistry meterRegistry) {
        return new LatencyMeterConfigurer(meterRegistry);
    }

    public LatencyMeterConfigurer enabled(boolean enabled) {
        configuredObject.setEnabled(enabled);
        return this;
    }
}
