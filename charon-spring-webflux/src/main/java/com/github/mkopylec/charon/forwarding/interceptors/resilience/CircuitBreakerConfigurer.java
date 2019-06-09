package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.micrometer.core.instrument.MeterRegistry;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry.of;

public class CircuitBreakerConfigurer extends RequestForwardingInterceptorConfigurer<CircuitBreaker> {

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

    public CircuitBreakerConfigurer meterRegistry(MeterRegistry meterRegistry) {
        configuredObject.setMeterRegistry(meterRegistry);
        return this;
    }
}
