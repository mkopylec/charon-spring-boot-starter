package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry.of;

public class CircuitBreakerConfigurer extends ResilienceHandlerConfigurer<CircuitBreaker, CircuitBreakerConfigurer> {

    private CircuitBreakerConfigurer() {
        super(new CircuitBreaker());
    }

    public static CircuitBreakerConfigurer circuitBreaker() {
        return new CircuitBreakerConfigurer();
    }

    public CircuitBreakerConfigurer configuration(CircuitBreakerConfig.Builder circuitBreakerConfigBuilder) {
        configuredObject.setRegistry(of(circuitBreakerConfigBuilder.build()));
        return this;
    }
}
