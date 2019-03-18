package com.github.mkopylec.charon.configuration;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;

public class RateLimiterConfigurer {

    private RateLimiterConfiguration rateLimiterConfiguration;

    private RateLimiterConfigurer() {
        rateLimiterConfiguration = new RateLimiterConfiguration();
    }

    public static RateLimiterConfigurer rateLimiter() {
        return new RateLimiterConfigurer();
    }

    public RateLimiterConfigurer configure(RateLimiterConfig.Builder rateLimiterConfigBuilder) {
        rateLimiterConfiguration.setRateLimiterConfig(rateLimiterConfigBuilder.build());
        return this;
    }

    public RateLimiterConfigurer measured(boolean measured) {
        rateLimiterConfiguration.setMeasured(measured);
        return this;
    }

    RateLimiterConfiguration getConfiguration() {
        return rateLimiterConfiguration;
    }
}
