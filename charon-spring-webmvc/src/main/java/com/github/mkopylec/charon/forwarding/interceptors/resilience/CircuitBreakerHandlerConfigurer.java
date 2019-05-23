package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry.of;

public class CircuitBreakerHandlerConfigurer extends ResilienceHandlerConfigurer<CircuitBreakerHandler, CircuitBreakerHandlerConfigurer> {

    private CircuitBreakerHandlerConfigurer() {
        super(new CircuitBreakerHandler());
    }

    public static CircuitBreakerHandlerConfigurer circuitBreakerHandler() {
        return new CircuitBreakerHandlerConfigurer();
    }

    public CircuitBreakerHandlerConfigurer configuration(CircuitBreakerConfig.Builder circuitBreakerConfigBuilder) {
        configuredObject.setRegistry(of(circuitBreakerConfigBuilder.build()));
        return this;
    }
}
