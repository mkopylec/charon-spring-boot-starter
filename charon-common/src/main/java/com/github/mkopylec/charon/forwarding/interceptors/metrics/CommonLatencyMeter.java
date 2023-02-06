package com.github.mkopylec.charon.forwarding.interceptors.metrics;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import org.slf4j.Logger;

import java.time.Duration;

import static com.github.mkopylec.charon.forwarding.Utils.metricName;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.LATENCY_METER;
import static java.lang.System.nanoTime;
import static java.time.Duration.ofNanos;

abstract class CommonLatencyMeter extends CommonMeter {

    CommonLatencyMeter(Logger log) {
        super(log);
    }

    public RequestForwardingInterceptorType getType() {
        return LATENCY_METER;
    }

    void captureLatencyMetric(String mappingName, long startingTime) {
        String metricName = metricName(mappingName, "latency");
        Duration responseTime = ofNanos(nanoTime() - startingTime);
        getMeterRegistry().timer(metricName).record(responseTime);
    }

    void logStart(String mappingName) {
        getLog().trace("[Start] Collect latency metrics of '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        getLog().trace("[End] Collect latency metrics of '{}' request mapping", mappingName);
    }
}
