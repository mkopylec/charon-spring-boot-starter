package com.github.mkopylec.charon.application;

import java.util.SortedMap;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.codahale.metrics.MetricFilter.ALL;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * This class in not thread safe.
 */
@Component
public class TestMetricsReporter extends ScheduledReporter {

    private boolean metricsCaptured = false;

    @Autowired
    public TestMetricsReporter(MetricRegistry registry) {
        super(registry, "test-reporter", ALL, SECONDS, MILLISECONDS);
    }

    @Override
    public void report(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters, SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters, SortedMap<String, Timer> timers) {
        metricsCaptured = true;
    }

    public boolean isMetricsCaptured() {
        return metricsCaptured;
    }
}
