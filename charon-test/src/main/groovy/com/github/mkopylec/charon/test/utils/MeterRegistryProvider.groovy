package com.github.mkopylec.charon.test.utils

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry

class MeterRegistryProvider {

    private static MeterRegistry meterRegistry = new SimpleMeterRegistry()

    static MeterRegistry meterRegistry() {
        return meterRegistry
    }

    static void clearMetrics() {
        meterRegistry.clear()
    }
}
