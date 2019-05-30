package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import io.github.resilience4j.retry.RetryConfig;

import static io.github.resilience4j.retry.RetryRegistry.of;

public class RetryerConfigurer extends ResilienceHandlerConfigurer<Retryer, RetryerConfigurer> {

    private RetryerConfigurer() {
        super(new Retryer());
    }

    public static RetryerConfigurer retryer() {
        return new RetryerConfigurer();
    }

    public RetryerConfigurer configuration(RetryConfig.Builder retryConfigBuilder) {
        configuredObject.setRegistry(of(retryConfigBuilder.build()));
        return this;
    }
}
