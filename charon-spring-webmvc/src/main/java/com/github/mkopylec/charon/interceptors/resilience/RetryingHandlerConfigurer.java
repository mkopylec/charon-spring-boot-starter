package com.github.mkopylec.charon.interceptors.resilience;

import io.github.resilience4j.retry.RetryConfig;

public class RetryingHandlerConfigurer extends ResilienceHandlerConfigurer<RetryingHandler, RetryingHandlerConfigurer> {

    private RetryingHandlerConfigurer() {
        super(new RetryingHandler());
    }

    public static RetryingHandlerConfigurer retryingHandler() {
        return new RetryingHandlerConfigurer();
    }

    public RetryingHandlerConfigurer configuration(RetryConfig.Builder retryConfigBuilder) {
        configuredObject.setConfiguration(retryConfigBuilder.build());
        return this;
    }
}
