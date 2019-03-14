package com.github.mkopylec.charon.configuration;

public class RequestForwardingRateLimiterConfigurer {

    private RequestForwardingRateLimiterConfiguration requestForwardingRateLimiterConfiguration;

    private RequestForwardingRateLimiterConfigurer() {
        requestForwardingRateLimiterConfiguration = new RequestForwardingRateLimiterConfiguration();
    }

    public static RequestForwardingRateLimiterConfigurer requestForwardingRateLimiter() {
        return new RequestForwardingRateLimiterConfigurer();
    }

    public RequestForwardingRateLimiterConfigurer enabled(boolean enabled) {
        requestForwardingRateLimiterConfiguration.setEnabled(enabled);
        return this;
    }

    public RequestForwardingRateLimiterConfigurer configure(RateLimiterConfigurer rateLimiterConfigurer) {
        requestForwardingRateLimiterConfiguration.setRateLimiterConfiguration(rateLimiterConfigurer.getConfiguration());
        return this;
    }

    RequestForwardingRateLimiterConfiguration getConfiguration() {
        return requestForwardingRateLimiterConfiguration;
    }
}
