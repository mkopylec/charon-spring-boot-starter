package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import io.github.resilience4j.retry.RetryConfig;

import static io.github.resilience4j.retry.RetryRegistry.of;

public class RetryingHandlerConfigurer extends ResilienceHandlerConfigurer<RetryingHandler, RetryingHandlerConfigurer> {

    private RetryingHandlerConfigurer() {
        super(new RetryingHandler());
    }

    public static RetryingHandlerConfigurer retryingHandler() {
        return new RetryingHandlerConfigurer();
    }

    public RetryingHandlerConfigurer configuration(RetryConfig.Builder retryConfigBuilder) {
        configuredObject.setRegistry(of(retryConfigBuilder.build()));
        return this;
    }
}
