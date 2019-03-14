package com.github.mkopylec.charon.configuration;

public class RequestForwardingRateLimiterConfiguration {

    private boolean enabled;
    private RateLimiterConfiguration rateLimiterConfiguration;

    RequestForwardingRateLimiterConfiguration() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public RateLimiterConfiguration getRateLimiterConfiguration() {
        return rateLimiterConfiguration;
    }

    void setRateLimiterConfiguration(RateLimiterConfiguration rateLimiterConfiguration) {
        this.rateLimiterConfiguration = rateLimiterConfiguration;
    }
}
