package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.configuration.Valid;
import io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics.MetricNames;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.core.Ordered;

import java.util.function.Predicate;

import static com.github.mkopylec.charon.forwarding.Utils.metricName;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RETRYING_HANDLER;
import static io.github.resilience4j.micrometer.tagged.TaggedRetryMetrics.ofRetryRegistry;
import static io.github.resilience4j.retry.RetryRegistry.of;
import static java.time.Duration.ofMillis;

abstract class BasicRetryer extends BasicResilienceHandler<RetryRegistry> implements Ordered, Valid {

    private static final String RETRYING_METRICS_NAME = "retrying";

    BasicRetryer(Predicate<Object> retryOnResult) {
        super(of(RetryConfig.custom()
                .waitDuration(ofMillis(10))
                .retryOnResult(retryOnResult)
                .build()));
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
}
