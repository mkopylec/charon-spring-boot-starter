package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.MetricsUtils;
import io.github.resilience4j.micrometer.tagged.TaggedRateLimiterMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedRateLimiterMetrics.MetricNames;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.slf4j.Logger;

import static io.github.resilience4j.micrometer.tagged.TaggedRateLimiterMetrics.ofRateLimiterRegistry;
import static io.github.resilience4j.ratelimiter.RateLimiterConfig.custom;
import static io.github.resilience4j.ratelimiter.RateLimiterRegistry.of;
import static org.slf4j.LoggerFactory.getLogger;

class RateLimitingHandler extends ResilienceHandler<RateLimiterRegistry> {

    private static final String RATE_LIMITING_METRICS_NAME = "rate-limiting";

    private static final Logger log = getLogger(RateLimitingHandler.class);

    RateLimitingHandler() {
        super(of(custom().build()));
    }

    @Override
    protected HttpResponse forwardRequest(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Rate limiting of '{}' request mapping", execution.getMappingName());
        RateLimiter rateLimiter = registry.rateLimiter(execution.getMappingName());
        setupMetrics(registry -> createMetrics(registry, execution.getMappingName()));
        HttpResponse response = rateLimiter.executeSupplier(() -> execution.execute(request));
        log.trace("[End] Rate limiting of '{}' request mapping", execution.getMappingName());
        return response;
    }

    @Override
    public int getOrder() {
        return RATE_LIMITING_HANDLER_ORDER;
    }

    private TaggedRateLimiterMetrics createMetrics(RateLimiterRegistry registry, String mappingName) {
        String availablePermissionsMetricName = MetricsUtils.metricName(mappingName, RATE_LIMITING_METRICS_NAME, "available-permissions");
        String waitingThreadsMetricName = MetricsUtils.metricName(mappingName, RATE_LIMITING_METRICS_NAME, "waiting-threads");
        MetricNames metricNames = MetricNames.custom()
                .availablePermissionsMetricName(availablePermissionsMetricName)
                .waitingThreadsMetricName(waitingThreadsMetricName)
                .build();
        return ofRateLimiterRegistry(metricNames, registry);
    }
}
