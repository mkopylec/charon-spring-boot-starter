package com.github.mkopylec.charon.forwarding.interceptors.metrics;

import org.slf4j.Logger;

import org.springframework.http.HttpStatus;

import static com.github.mkopylec.charon.forwarding.Utils.metricName;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RATE_METER;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCause;

abstract class BasicRateMeter extends BasicMeter {

    BasicRateMeter(Logger log) {
        super(log);
    }

    @Override
    public int getOrder() {
        return RATE_METER.getOrder();
    }

    void captureResponseStatusMetric(String mappingName, HttpStatus responseStatus) {
        String metricName = metricName(mappingName, "response", "status", valueOf(responseStatus.value()));
        getMeterRegistry().counter(metricName).increment();
    }

    void captureExceptionMetric(String mappingName, Throwable ex) {
        String exception = getRootCause(ex).getClass().getSimpleName().toLowerCase();
        String metricName = metricName(mappingName, "response", "exception", exception);
        getMeterRegistry().counter(metricName).increment();
    }

    void logStart(String mappingName) {
        getLog().trace("[Start] Collect rate metrics of '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        getLog().trace("[End] Collect rate metrics of '{}' request mapping", mappingName);
    }
}
