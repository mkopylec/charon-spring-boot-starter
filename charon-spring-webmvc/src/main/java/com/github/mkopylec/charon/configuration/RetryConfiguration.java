package com.github.mkopylec.charon.configuration;

import io.github.resilience4j.retry.RetryConfig;

import org.springframework.web.client.HttpClientErrorException;

import static io.github.resilience4j.retry.RetryConfig.custom;
import static java.time.Duration.ofMillis;

public class RetryConfiguration {

    private RetryConfig retryConfig;
    private boolean measured;

    RetryConfiguration() {
        retryConfig = custom()
                .waitDuration(ofMillis(10))
                .ignoreExceptions(HttpClientErrorException.class)
                .build();
    }

    public RetryConfig getRetryConfig() {
        return retryConfig;
    }

    void setRetryConfig(RetryConfig retryConfig) {
        this.retryConfig = retryConfig;
    }

    public boolean isMeasured() {
        return measured;
    }

    void setMeasured(boolean measured) {
        this.measured = measured;
    }
}
