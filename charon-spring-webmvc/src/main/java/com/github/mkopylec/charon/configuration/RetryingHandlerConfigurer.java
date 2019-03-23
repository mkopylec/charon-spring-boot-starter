package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.core.interceptors.RetryingHandler;
import io.github.resilience4j.retry.RetryConfig;

public class RetryingHandlerConfigurer extends RequestForwardingInterceptorConfigurer<RetryingHandler> {

    protected RetryingHandlerConfigurer() {
        super(new RetryingHandler());
    }

    public static RetryingHandlerConfigurer retryingHandler() {
        return new RetryingHandlerConfigurer();
    }

    public RetryingHandlerConfigurer enabled(boolean enabled) {
        configure(retryingHandler -> retryingHandler.setEnabled(enabled));
        return this;
    }

    public RetryingHandlerConfigurer configuration(RetryConfig.Builder retryConfigBuilder) {
        configure(retryingHandler -> retryingHandler.setRetryConfig(retryConfigBuilder.build()));
        return this;
    }

    public RetryingHandlerConfigurer measured(boolean measured) {
        configure(retryingHandler -> retryingHandler.setMeasured(measured));
        return this;
    }
}
