package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;

import static io.github.resilience4j.ratelimiter.RateLimiterRegistry.of;

public class RateLimitingHandlerConfigurer extends ResilienceHandlerConfigurer<RateLimitingHandler, RateLimitingHandlerConfigurer> {

    private RateLimitingHandlerConfigurer() {
        super(new RateLimitingHandler());
    }

    public static RateLimitingHandlerConfigurer rateLimitingHandler() {
        return new RateLimitingHandlerConfigurer();
    }

    public RateLimitingHandlerConfigurer configuration(RateLimiterConfig.Builder rateLimiterConfigBuilder) {
        configuredObject.setRegistry(of(rateLimiterConfigBuilder.build()));
        return this;
    }
}
