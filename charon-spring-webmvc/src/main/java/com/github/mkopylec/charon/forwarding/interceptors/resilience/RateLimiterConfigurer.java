package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;
import io.github.resilience4j.ratelimiter.RateLimiterConfig.Builder;
import io.micrometer.core.instrument.MeterRegistry;

import static io.github.resilience4j.ratelimiter.RateLimiterRegistry.of;

public class RateLimiterConfigurer extends RequestForwardingInterceptorConfigurer<RateLimiter> {

    private RateLimiterConfigurer() {
        super(new RateLimiter());
    }

    public static RateLimiterConfigurer rateLimiter() {
        return new RateLimiterConfigurer();
    }

    public RateLimiterConfigurer configuration(Builder rateLimiterConfiguration) {
        configuredObject.setRegistry(of(rateLimiterConfiguration.build()));
        return this;
    }

    public RateLimiterConfigurer meterRegistry(MeterRegistry meterRegistry) {
        configuredObject.setMeterRegistry(meterRegistry);
        return this;
    }
}
