package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;

import static io.github.resilience4j.ratelimiter.RateLimiterRegistry.of;

public class RateLimiterConfigurer extends ResilienceHandlerConfigurer<RateLimiter, RateLimiterConfigurer> {

    private RateLimiterConfigurer() {
        super(new RateLimiter());
    }

    public static RateLimiterConfigurer rateLimiter() {
        return new RateLimiterConfigurer();
    }

    public RateLimiterConfigurer configuration(RateLimiterConfig.Builder rateLimiterConfigBuilder) {
        configuredObject.setRegistry(of(rateLimiterConfigBuilder.build()));
        return this;
    }
}
