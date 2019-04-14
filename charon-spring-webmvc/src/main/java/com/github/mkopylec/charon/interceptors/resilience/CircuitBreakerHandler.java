package com.github.mkopylec.charon.interceptors.resilience;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.CircuitBreakerMetrics;
import org.slf4j.Logger;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom;
import static io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry.of;
import static io.github.resilience4j.micrometer.CircuitBreakerMetrics.ofCircuitBreakerRegistry;
import static org.slf4j.LoggerFactory.getLogger;

class CircuitBreakerHandler extends ResilienceHandler<CircuitBreakerRegistry> {

    private static final Logger log = getLogger(CircuitBreakerHandler.class);

    private CircuitBreakerFallback circuitBreakerFallback;

    CircuitBreakerHandler() {
        // TODO Handle 5xx after https://github.com/resilience4j/resilience4j/issues/384 is done
        super(of(custom().build()));
    }

    @Override
    protected HttpResponse forwardRequest(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Circuit breaker for '{}' request mapping", execution.getMappingName());
        CircuitBreaker circuitBreaker = registry.circuitBreaker(execution.getMappingName());
        setupMetrics(this::createMetrics);
        HttpResponse response = getResponse(request, execution, circuitBreaker);
        log.trace("[End] Circuit breaker for '{}' request mapping", execution.getMappingName());
        return response;
    }

    @Override
    public int getOrder() {
        return CIRCUIT_BREAKER_HANDLER_ORDER;
    }

    void setCircuitBreakerFallback(CircuitBreakerFallback circuitBreakerFallback) {
        this.circuitBreakerFallback = circuitBreakerFallback;
    }

    private CircuitBreakerMetrics createMetrics(CircuitBreakerRegistry registry) {
        // TODO Wait for 0.14.0 version to be able to customize metric names
        return ofCircuitBreakerRegistry(registry);
    }

    private HttpResponse getResponse(HttpRequest request, HttpRequestExecution execution, CircuitBreaker circuitBreaker) {
        HttpResponse response;
        try {
            response = circuitBreaker.executeSupplier(() -> execution.execute(request));
        } catch (RuntimeException e) { // TODO Check what exception type is thrown
            response = handleError(e, execution.getMappingName());
        }
        return response;
    }

    private HttpResponse handleError(RuntimeException e, String forwardingName) {
        if (circuitBreakerFallback != null) {
            log.debug("Executing circuit breaker fallback of '{}' request mapping", forwardingName);
            return circuitBreakerFallback.run(e);
        }
        throw e;
    }
}
