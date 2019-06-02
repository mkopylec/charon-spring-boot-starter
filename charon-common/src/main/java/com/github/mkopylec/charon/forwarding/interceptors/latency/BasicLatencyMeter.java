package com.github.mkopylec.charon.forwarding.interceptors.latency;

import java.time.Duration;

import com.github.mkopylec.charon.configuration.Valid;
import io.micrometer.core.instrument.MeterRegistry;

import org.springframework.core.Ordered;

import static com.github.mkopylec.charon.forwarding.Utils.metricName;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.LATENCY_METER;
import static java.lang.System.nanoTime;
import static java.time.Duration.ofNanos;
import static org.springframework.util.Assert.notNull;

class BasicLatencyMeter implements Ordered, Valid {

    private MeterRegistry meterRegistry;

    BasicLatencyMeter() {
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
}
