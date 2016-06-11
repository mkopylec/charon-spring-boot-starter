package com.github.mkopylec.charon.assertions

import com.github.mkopylec.charon.application.TestMetricsReporter

class MetricsAssert {

    private TestMetricsReporter actual

    protected MetricsAssert(TestMetricsReporter actual) {
        assert actual != null
        this.actual = actual
    }

    MetricsAssert hasCapturedMetrics() {
        assert actual.isMetricsCaptured()
        return this
    }
}
