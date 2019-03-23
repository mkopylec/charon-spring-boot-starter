package com.github.mkopylec.charon.core.interceptors;

import com.github.mkopylec.charon.core.HttpRequest;
import com.github.mkopylec.charon.core.HttpResponse;
import com.github.mkopylec.charon.core.RequestForwarder;
import com.github.mkopylec.charon.core.RequestForwarding;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom;

public class CircuitBreakerHandler implements RequestForwardingInterceptor {

    protected boolean enabled;
    protected CircuitBreakerConfig circuitBreakerConfig;
    protected boolean measured;

    public CircuitBreakerHandler() {
        enabled = true;
        circuitBreakerConfig = custom().build();
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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setCircuitBreakerConfig(CircuitBreakerConfig circuitBreakerConfig) {
        this.circuitBreakerConfig = circuitBreakerConfig;
    }

    public void setMeasured(boolean measured) {
        this.measured = measured;
    }
}
