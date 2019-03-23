package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.core.interceptors.CircuitBreakerHandler;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

public class CircuitBreakerHandlerConfigurer extends RequestForwardingInterceptorConfigurer<CircuitBreakerHandler> {

    protected CircuitBreakerHandlerConfigurer() {
        super(new CircuitBreakerHandler());
    }

    public static CircuitBreakerHandlerConfigurer circuitBreakerHandler() {
        return new CircuitBreakerHandlerConfigurer();
    }

    public CircuitBreakerHandlerConfigurer enabled(boolean enabled) {
        configure(retryingHandler -> retryingHandler.setEnabled(enabled));
        return this;
    }

    public CircuitBreakerHandlerConfigurer configuration(CircuitBreakerConfig.Builder circuitBreakerConfigBuilder) {
        configure(circuitBreakerHandler -> circuitBreakerHandler.setCircuitBreakerConfig(circuitBreakerConfigBuilder.build()));
        return this;
    }

    public CircuitBreakerHandlerConfigurer measured(boolean measured) {
        configure(retryingHandler -> retryingHandler.setMeasured(measured));
        return this;
    }
}
