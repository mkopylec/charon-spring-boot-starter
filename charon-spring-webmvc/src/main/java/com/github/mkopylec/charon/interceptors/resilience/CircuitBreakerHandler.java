package com.github.mkopylec.charon.interceptors.resilience;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom;

class CircuitBreakerHandler extends ResilienceHandler<CircuitBreakerConfig> {

    CircuitBreakerHandler() {
        super(custom().build());
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        // TODO Implement circuit breaker with fallback
        return null;
    }

    @Override
    public int getOrder() {
        return CIRCUIT_BREAKER_HANDLER_ORDER;
    }
}
