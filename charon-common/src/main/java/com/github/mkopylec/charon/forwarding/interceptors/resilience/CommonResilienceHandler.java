package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.slf4j.Logger;

import java.util.function.Function;

abstract class CommonResilienceHandler<R> {

    private Logger log;
    private R registry;
    private MeterBinder metrics;
    private MeterRegistry meterRegistry;

    CommonResilienceHandler(Logger log, R registry) {
        this.log = log;
        this.registry = registry;
    }

    Logger getLog() {
        return log;
    }

    R getRegistry() {
        return registry;
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
