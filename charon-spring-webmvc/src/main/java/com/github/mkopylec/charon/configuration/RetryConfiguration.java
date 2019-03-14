package com.github.mkopylec.charon.configuration;

import io.github.resilience4j.retry.RetryConfig;

import static io.github.resilience4j.retry.RetryConfig.custom;
import static java.time.Duration.ZERO;

public class RetryConfiguration {

    private RetryConfig retryConfig;
    private boolean metricsEnabled;

    RetryConfiguration() {
        retryConfig = custom()
                .waitDuration(ZERO)
                // TODO Handle retry on 4xx and 5xx ?
                .build();
    }

    public RetryConfig getRetryConfig() {
        return retryConfig;
    }

    void setRetryConfig(RetryConfig retryConfig) {
        this.retryConfig = retryConfig;
    }

    public boolean isMetricsEnabled() {
        return metricsEnabled;
    }

    void setMetricsEnabled(boolean metricsEnabled) {
        this.metricsEnabled = metricsEnabled;
    }
}
