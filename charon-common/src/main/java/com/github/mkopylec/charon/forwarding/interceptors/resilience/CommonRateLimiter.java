package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import io.github.resilience4j.micrometer.tagged.TaggedRateLimiterMetrics;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.slf4j.Logger;

import static com.github.mkopylec.charon.forwarding.Utils.metricName;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RATE_LIMITING_HANDLER;
import static io.github.resilience4j.micrometer.tagged.TaggedRateLimiterMetrics.ofRateLimiterRegistry;
import static io.github.resilience4j.ratelimiter.RateLimiterConfig.custom;
import static io.github.resilience4j.ratelimiter.RateLimiterRegistry.of;
import static java.time.Duration.ZERO;
import static java.time.Duration.ofSeconds;

abstract class CommonRateLimiter extends CommonResilienceHandler<RateLimiterRegistry> implements Valid {

    private static final String RATE_LIMITING_METRICS_NAME = "rate-limiting";

    CommonRateLimiter(Logger log) {
        super(log, of(custom()
                .timeoutDuration(ZERO)
                .limitRefreshPeriod(ofSeconds(1))
                .limitForPeriod(100)
                .build()));
    }

    public RequestForwardingInterceptorType getType() {
        return RATE_LIMITING_HANDLER;
    }

    TaggedRateLimiterMetrics createMetrics(RateLimiterRegistry registry, String mappingName) {
        String availablePermissionsMetricName = metricName(mappingName, RATE_LIMITING_METRICS_NAME, "available-permissions");
        String waitingThreadsMetricName = metricName(mappingName, RATE_LIMITING_METRICS_NAME, "waiting-threads");
        TaggedRateLimiterMetrics.MetricNames metricNames = TaggedRateLimiterMetrics.MetricNames.custom()
                .availablePermissionsMetricName(availablePermissionsMetricName)
                .waitingThreadsMetricName(waitingThreadsMetricName)
                .build();
        return ofRateLimiterRegistry(metricNames, registry);
    }

    void logStart(String mappingName) {
        getLog().trace("[Start] Rate limiting of '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        getLog().trace("[End] Rate limiting of '{}' request mapping", mappingName);
    }
}
