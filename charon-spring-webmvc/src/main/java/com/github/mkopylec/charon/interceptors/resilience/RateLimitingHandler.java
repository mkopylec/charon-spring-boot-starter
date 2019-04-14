package com.github.mkopylec.charon.interceptors.resilience;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import io.github.resilience4j.micrometer.RateLimiterMetrics;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.slf4j.Logger;

import static io.github.resilience4j.micrometer.RateLimiterMetrics.ofRateLimiterRegistry;
import static io.github.resilience4j.ratelimiter.RateLimiterConfig.custom;
import static io.github.resilience4j.ratelimiter.RateLimiterRegistry.of;
import static org.slf4j.LoggerFactory.getLogger;

class RateLimitingHandler extends ResilienceHandler<RateLimiterRegistry> {

    private static final Logger log = getLogger(RateLimitingHandler.class);

    RateLimitingHandler() {
        super(of(custom().build()));
    }

    @Override
    protected HttpResponse forwardRequest(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Rate limiting of '{}' request mapping", execution.getMappingName());
        RateLimiter rateLimiter = registry.rateLimiter(execution.getMappingName());
        setupMetrics(this::createMetrics);
        HttpResponse response = rateLimiter.executeSupplier(() -> execution.execute(request));
        log.trace("[End] Rate limiting of '{}' request mapping", execution.getMappingName());
        return response;
    }

    @Override
    public int getOrder() {
        return RATE_LIMITING_HANDLER_ORDER;
    }

    private RateLimiterMetrics createMetrics(RateLimiterRegistry registry) {
        // TODO Wait for 0.14.0 version to be able to customize metric names
        return ofRateLimiterRegistry(registry);
    }
}
