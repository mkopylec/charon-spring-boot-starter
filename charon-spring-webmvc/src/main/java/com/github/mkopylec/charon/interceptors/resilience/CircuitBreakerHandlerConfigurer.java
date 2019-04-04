package com.github.mkopylec.charon.interceptors.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

public class CircuitBreakerHandlerConfigurer extends ResilienceHandlerConfigurer<CircuitBreakerHandler, CircuitBreakerHandlerConfigurer> {

    private CircuitBreakerHandlerConfigurer() {
        super(new CircuitBreakerHandler());
    }

    public static CircuitBreakerHandlerConfigurer circuitBreakerHandler() {
        return new CircuitBreakerHandlerConfigurer();
    }

    public CircuitBreakerHandlerConfigurer configuration(CircuitBreakerConfig.Builder circuitBreakerConfigBuilder) {
        configuredObject.setRegistry(CircuitBreakerRegistry.of(circuitBreakerConfigBuilder.build()));
        return this;
    }

    public CircuitBreakerHandlerConfigurer circuitBreakerFallback(CircuitBreakerFallback circuitBreakerFallback) {
        configuredObject.setCircuitBreakerFallback(circuitBreakerFallback);
        return this;
    }
}
