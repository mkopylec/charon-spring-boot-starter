package com.github.mkopylec.charon.assertions

import com.github.mkopylec.charon.application.GraphiteServerMock

class MetricsAssert {

    private GraphiteServerMock actual

    protected MetricsAssert(GraphiteServerMock actual) {
        assert actual != null
        this.actual = actual
    }

    MetricsAssert hasCapturedMetrics() {
        assert actual.isMetricsCaptured()
        return this
    }
}
