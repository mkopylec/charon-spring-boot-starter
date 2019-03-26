package com.github.mkopylec.charon.interceptors.resilience;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;

public class RateLimitingHandlerConfigurer extends ResilienceHandlerConfigurer<RateLimitingHandler, RateLimitingHandlerConfigurer> {

    private RateLimitingHandlerConfigurer() {
        super(new RateLimitingHandler());
    }

    public static RateLimitingHandlerConfigurer rateLimitingHandler() {
        return new RateLimitingHandlerConfigurer();
    }

    public RateLimitingHandlerConfigurer configuration(RateLimiterConfig.Builder rateLimiterConfigBuilder) {
        configuredObject.setConfiguration(rateLimiterConfigBuilder.build());
        return this;
    }
}
