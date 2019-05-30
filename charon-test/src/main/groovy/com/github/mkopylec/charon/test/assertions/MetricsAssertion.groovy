package com.github.mkopylec.charon.test.assertions

import io.micrometer.core.instrument.MeterRegistry

class MetricsAssertion {

    private MeterRegistry meterRegistry

    protected MetricsAssertion(MeterRegistry meterRegistry) {
        assert meterRegistry != null
        this.meterRegistry = meterRegistry
    }

    MetricsAssertion haveCaptured(String metricsName) {
        assert meterRegistry.find(metricsName)
        return this
    }

    MetricsAssertion haveCapturedNothing() {
        assert meterRegistry.meters.isEmpty()
        return this
    }
}
