package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

class CircuitBreaker extends BasicCircuitBreaker<HttpResponse> implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(CircuitBreaker.class);

    CircuitBreaker() {
        super(log);
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = registry.circuitBreaker(execution.getMappingName());
        setupMetrics(registry -> createMetrics(registry, execution.getMappingName()));
        HttpResponse response;
        try {
            response = circuitBreaker.executeSupplier(() -> execution.execute(request));
        } catch (CallNotPermittedException ex) {
            response = executeFallback(ex);
        }
        logEnd(execution.getMappingName());
        return response;
    }
}
