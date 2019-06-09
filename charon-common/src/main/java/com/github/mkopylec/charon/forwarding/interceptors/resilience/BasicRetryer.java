package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import java.util.function.Predicate;

import com.github.mkopylec.charon.configuration.Valid;
import io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics.MetricNames;
import io.github.resilience4j.retry.RetryRegistry;
import org.slf4j.Logger;

import org.springframework.core.Ordered;

import static com.github.mkopylec.charon.forwarding.Utils.metricName;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RETRYING_HANDLER;
import static io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics.ofRetryRegistry;
import static io.github.resilience4j.retry.RetryConfig.custom;
import static io.github.resilience4j.retry.RetryRegistry.of;
import static java.time.Duration.ofMillis;

abstract class BasicRetryer extends BasicResilienceHandler<RetryRegistry> implements Ordered, Valid {

    private static final String RETRYING_METRICS_NAME = "retrying";

    private Logger log;

    BasicRetryer(Predicate<Object> retryOnResult, Logger log) {
        super(of(custom()
                .waitDuration(ofMillis(10))
                .retryOnResult(retryOnResult)
                .retryOnException(throwable -> true)
                .build()));
        this.log = log;
    }

    @Override
    public int getOrder() {
        return RETRYING_HANDLER.getOrder();
    }

    TaggedRetryMetrics createMetrics(RetryRegistry registry, String mappingName) {
        String callsMetricName = metricName(mappingName, RETRYING_METRICS_NAME, "calls");
        MetricNames metricNames = MetricNames.custom()
                .callsMetricName(callsMetricName)
                .build();
        return ofRetryRegistry(metricNames, registry);
    }

    void logStart(String mappingName) {
        log.trace("[Start] Retrying of '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Retrying of '{}' request mapping", mappingName);
    }
}
