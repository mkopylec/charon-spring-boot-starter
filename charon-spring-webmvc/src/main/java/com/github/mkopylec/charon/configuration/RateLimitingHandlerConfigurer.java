package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.core.interceptors.RateLimitingHandler;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;

public class RateLimitingHandlerConfigurer extends RequestForwardingInterceptorConfigurer<RateLimitingHandler> {

    protected RateLimitingHandlerConfigurer() {
        super(new RateLimitingHandler());
    }

    public static RateLimitingHandlerConfigurer rateLimitingHandler() {
        return new RateLimitingHandlerConfigurer();
    }

    public RateLimitingHandlerConfigurer enabled(boolean enabled) {
        configure(retryingHandler -> retryingHandler.setEnabled(enabled));
        return this;
    }

    public RateLimitingHandlerConfigurer configuration(RateLimiterConfig.Builder rateLimiterConfigBuilder) {
        configure(rateLimitingHandler -> rateLimitingHandler.setRateLimiterConfig(rateLimiterConfigBuilder.build()));
        return this;
    }

    public RateLimitingHandlerConfigurer measured(boolean measured) {
        configure(retryingHandler -> retryingHandler.setMeasured(measured));
        return this;
    }
}
