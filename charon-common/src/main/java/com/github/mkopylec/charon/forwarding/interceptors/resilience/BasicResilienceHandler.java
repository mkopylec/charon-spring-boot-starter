package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import java.util.function.Function;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;

abstract class BasicResilienceHandler<R> {

    R registry;
    private MeterBinder metrics;
    private MeterRegistry meterRegistry;

    BasicResilienceHandler(R registry) {
        this.registry = registry;
    }

    void setRegistry(R registry) {
        this.registry = registry;
    }

    void setMeterRegistry(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    void setupMetrics(Function<R, ? extends MeterBinder> metricsCreator) {
        if (meterRegistry == null) {
            return;
        }
        if (metrics == null) {
            metrics = metricsCreator.apply(registry);
            metrics.bindTo(meterRegistry);
        }
    }
}
