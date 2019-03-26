package com.github.mkopylec.charon.interceptors.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

public class CircuitBreakerHandlerConfigurer extends ResilienceHandlerConfigurer<CircuitBreakerHandler, CircuitBreakerHandlerConfigurer> {

    private CircuitBreakerHandlerConfigurer() {
        super(new CircuitBreakerHandler());
    }

    public static CircuitBreakerHandlerConfigurer circuitBreakerHandler() {
        return new CircuitBreakerHandlerConfigurer();
    }

    public CircuitBreakerHandlerConfigurer configuration(CircuitBreakerConfig.Builder circuitBreakerConfigBuilder) {
        configuredObject.setConfiguration(circuitBreakerConfigBuilder.build());
        return this;
    }
}
