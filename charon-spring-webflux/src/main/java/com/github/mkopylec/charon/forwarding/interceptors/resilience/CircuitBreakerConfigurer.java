package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import java.util.function.Function;

import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.Builder;
import io.micrometer.core.instrument.MeterRegistry;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry.of;

public class CircuitBreakerConfigurer extends RequestForwardingInterceptorConfigurer<CircuitBreaker> {

    private CircuitBreakerConfigurer() {
        super(new CircuitBreaker());
    }

    public static CircuitBreakerConfigurer circuitBreaker() {
        return new CircuitBreakerConfigurer();
    }

    public CircuitBreakerConfigurer configuration(Builder circuitBreakerConfiguration) {
        configuredObject.setRegistry(of(circuitBreakerConfiguration.build()));
        return this;
    }

    public CircuitBreakerConfigurer fallback(Function<CallNotPermittedException, HttpResponse> fallback) {
        configuredObject.setFallback(fallback);
        return this;
    }

    public CircuitBreakerConfigurer meterRegistry(MeterRegistry meterRegistry) {
        configuredObject.setMeterRegistry(meterRegistry);
        return this;
    }
}
