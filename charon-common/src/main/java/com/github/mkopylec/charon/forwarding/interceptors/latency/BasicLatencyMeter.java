package com.github.mkopylec.charon.forwarding.interceptors.latency;

import java.time.Duration;

import com.github.mkopylec.charon.configuration.Valid;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;

import org.springframework.core.Ordered;

import static com.github.mkopylec.charon.forwarding.Utils.metricName;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.LATENCY_METER;
import static java.lang.System.nanoTime;
import static java.time.Duration.ofNanos;
import static org.springframework.util.Assert.notNull;

abstract class BasicLatencyMeter implements Ordered, Valid {

    private Logger log;
    private MeterRegistry meterRegistry;

    BasicLatencyMeter(Logger log) {
        this.log = log;
    }

    @Override
    public void validate() {
        notNull(meterRegistry, "No meter registry set");
    }

    @Override
    public int getOrder() {
        return LATENCY_METER.getOrder();
    }

    void setMeterRegistry(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    void captureLatencyMetric(String mappingName, long startingTime) {
        String metricName = metricName(mappingName, "latency");
        Duration responseTime = ofNanos(nanoTime() - startingTime);
        meterRegistry.timer(metricName).record(responseTime);
    }

    void logStart(String mappingName) {
        log.trace("[Start] Collect metrics of '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Collect metrics of '{}' request mapping", mappingName);
    }
}
