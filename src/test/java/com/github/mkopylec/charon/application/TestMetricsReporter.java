package com.github.mkopylec.charon.application;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class TestMetricsReporter {

    private final MeterRegistry meterRegistry;

    public TestMetricsReporter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public boolean areMetricsCaptured(String metricsName) {
        return meterRegistry.timer(metricsName).count() == 1;
    }
}
