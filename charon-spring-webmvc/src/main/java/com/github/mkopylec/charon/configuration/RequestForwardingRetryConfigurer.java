package com.github.mkopylec.charon.configuration;

public class RequestForwardingRetryConfigurer {

    private RequestForwardingRetryConfiguration requestForwardingRetryConfiguration;

    private RequestForwardingRetryConfigurer() {
        requestForwardingRetryConfiguration = new RequestForwardingRetryConfiguration();
    }

    public static RequestForwardingRetryConfigurer requestForwardingRetry() {
        return new RequestForwardingRetryConfigurer();
    }

    public RequestForwardingRetryConfigurer enabled(boolean enabled) {
        requestForwardingRetryConfiguration.setEnabled(enabled);
        return this;
    }

    public RequestForwardingRetryConfigurer configure(RetryConfigurer retryConfigurer) {
        requestForwardingRetryConfiguration.setRetryConfiguration(retryConfigurer.getConfiguration());
        return this;
    }

    RequestForwardingRetryConfiguration getConfiguration() {
        return requestForwardingRetryConfiguration;
    }
}
