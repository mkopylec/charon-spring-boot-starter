package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.slf4j.Logger;

import java.util.function.Predicate;

import static com.github.mkopylec.charon.forwarding.Utils.metricName;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RETRYING_HANDLER;
import static io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics.ofRetryRegistry;
import static io.github.resilience4j.retry.RetryRegistry.of;
import static java.time.Duration.ZERO;

abstract class CommonRetryer<R> extends CommonResilienceHandler<RetryRegistry> implements Valid {

    private static final String RETRYING_METRICS_NAME = "retrying";

    CommonRetryer(Predicate<R> retryOnResult, Logger log) {
        super(log, of(RetryConfig.<R>custom()
                .waitDuration(ZERO)
                .retryOnResult(retryOnResult)
                .retryOnException(throwable -> true)
                .build()));
    }

    public RequestForwardingInterceptorType getType() {
        return RETRYING_HANDLER;
    }

    TaggedRetryMetrics createMetrics(RetryRegistry registry, String mappingName) {
        String callsMetricName = metricName(mappingName, RETRYING_METRICS_NAME, "calls");
        TaggedRetryMetrics.MetricNames metricNames = TaggedRetryMetrics.MetricNames.custom()
                .callsMetricName(callsMetricName)
                .build();
        return ofRetryRegistry(metricNames, registry);
    }

    void logStart(String mappingName) {
        getLog().trace("[Start] Retrying of '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        getLog().trace("[End] Retrying of '{}' request mapping", mappingName);
    }
}
