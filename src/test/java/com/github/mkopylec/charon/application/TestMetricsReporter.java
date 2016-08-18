package com.github.mkopylec.charon.application;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.SortedMap;

import static com.codahale.metrics.MetricFilter.ALL;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * This class in not thread safe.
 */
@Component
@ConditionalOnProperty("test.metrics-reporter-enabled")
public class TestMetricsReporter extends ScheduledReporter {

    private boolean metricsCaptured = false;

    @Autowired
    public TestMetricsReporter(MetricRegistry registry) {
        super(registry, "test-reporter", ALL, SECONDS, MILLISECONDS);
    }

    @Override
    public void report(SortedMap<String, Gauge> gauges, SortedMap<String, Counter> counters, SortedMap<String, Histogram> histograms, SortedMap<String, Meter> meters, SortedMap<String, Timer> timers) {
        if (!timers.isEmpty()) {
            metricsCaptured = timers.firstKey().startsWith("charon.");
        }
    }

    public boolean isMetricsCaptured() {
        return metricsCaptured;
    }

    @PostConstruct
    private void startCapturingMetrics() {
        start(1, SECONDS);
    }
}
