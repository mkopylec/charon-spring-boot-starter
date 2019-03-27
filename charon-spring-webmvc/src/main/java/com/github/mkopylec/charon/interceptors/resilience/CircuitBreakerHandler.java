package com.github.mkopylec.charon.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.HttpRequest;
import com.github.mkopylec.charon.forwarding.HttpResponse;
import com.github.mkopylec.charon.forwarding.RequestForwarder;
import com.github.mkopylec.charon.forwarding.RequestForwarding;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom;

class CircuitBreakerHandler extends ResilienceHandler<CircuitBreakerConfig> {

    CircuitBreakerHandler() {
        super(custom().build());
    }

    @Override
    public HttpResponse forward(HttpRequest request, RequestForwarding forwarding, RequestForwarder forwarder) {
        // TODO Implement circuit breaker
        return null;
    }

    @Override
    public int getOrder() {
        return CIRCUIT_BREAKER_HANDLER_ORDER;
    }
}
