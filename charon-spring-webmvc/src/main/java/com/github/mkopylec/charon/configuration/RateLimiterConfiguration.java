package com.github.mkopylec.charon.configuration;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;

import static io.github.resilience4j.ratelimiter.RateLimiterConfig.custom;

public class RateLimiterConfiguration {

    private RateLimiterConfig rateLimiterConfig;
    private boolean measured;

    RateLimiterConfiguration() {
        rateLimiterConfig = custom().build();
    }

    public RateLimiterConfig getRateLimiterConfig() {
        return rateLimiterConfig;
    }

    void setRateLimiterConfig(RateLimiterConfig rateLimiterConfig) {
        this.rateLimiterConfig = rateLimiterConfig;
    }

    public boolean isMeasured() {
        return measured;
    }

    void setMeasured(boolean measured) {
        this.measured = measured;
    }
}
