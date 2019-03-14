package com.github.mkopylec.charon.configuration;

public class RequestForwardingRetryConfiguration {

    private boolean enabled;
    private RetryConfiguration retryConfiguration;

    RequestForwardingRetryConfiguration() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public RetryConfiguration getRetryConfiguration() {
        return retryConfiguration;
    }

    void setRetryConfiguration(RetryConfiguration retryConfiguration) {
        this.retryConfiguration = retryConfiguration;
    }
}
