package com.github.mkopylec.charon.configuration;

import io.github.resilience4j.retry.RetryConfig;

public class RetryConfigurer {

    private RetryConfiguration retryConfiguration;

    private RetryConfigurer() {
        retryConfiguration = new RetryConfiguration();
    }

    public static RetryConfigurer retry() {
        return new RetryConfigurer();
    }

    public RetryConfigurer configure(RetryConfig.Builder retryConfigBuilder) {
        retryConfiguration.setRetryConfig(retryConfigBuilder.build());
        return this;
    }

    public RetryConfigurer metricsEnabled(boolean metricsEnabled) {
        retryConfiguration.setMetricsEnabled(metricsEnabled);
        return this;
    }

    RetryConfiguration getConfiguration() {
        return retryConfiguration;
    }
}
