package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import io.github.resilience4j.micrometer.tagged.TaggedRateLimiterMetrics;
import io.github.resilience4j.micrometer.tagged.TaggedRateLimiterMetrics.MetricNames;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.slf4j.Logger;

import static com.github.mkopylec.charon.forwarding.Utils.metricName;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RATE_LIMITING_HANDLER;
import static io.github.resilience4j.micrometer.tagged.TaggedRateLimiterMetrics.ofRateLimiterRegistry;
import static io.github.resilience4j.ratelimiter.RateLimiterConfig.custom;
import static io.github.resilience4j.ratelimiter.RateLimiterRegistry.of;
import static java.time.Duration.ZERO;
import static java.time.Duration.ofSeconds;
import static org.slf4j.LoggerFactory.getLogger;

class RateLimiter extends ResilienceHandler<RateLimiterRegistry> {

    private static final String RATE_LIMITING_METRICS_NAME = "rate-limiting";

    private static final Logger log = getLogger(RateLimiter.class);

    RateLimiter() {
        super(of(custom()
                .timeoutDuration(ZERO)
                .limitRefreshPeriod(ofSeconds(1))
                .limitForPeriod(100)
                .build()));
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Rate limiting of '{}' request mapping", execution.getMappingName());
        io.github.resilience4j.ratelimiter.RateLimiter rateLimiter = registry.rateLimiter(execution.getMappingName());
        setupMetrics(registry -> createMetrics(registry, execution.getMappingName()));
        HttpResponse response = rateLimiter.executeSupplier(() -> execution.execute(request));
        log.trace("[End] Rate limiting of '{}' request mapping", execution.getMappingName());
        return response;
    }

    @Override
    public int getOrder() {
        return RATE_LIMITING_HANDLER.getOrder();
    }

    private TaggedRateLimiterMetrics createMetrics(RateLimiterRegistry registry, String mappingName) {
        String availablePermissionsMetricName = metricName(mappingName, RATE_LIMITING_METRICS_NAME, "available-permissions");
        String waitingThreadsMetricName = metricName(mappingName, RATE_LIMITING_METRICS_NAME, "waiting-threads");
        MetricNames metricNames = MetricNames.custom()
                .availablePermissionsMetricName(availablePermissionsMetricName)
                .waitingThreadsMetricName(waitingThreadsMetricName)
                .build();
        return ofRateLimiterRegistry(metricNames, registry);
    }
}
