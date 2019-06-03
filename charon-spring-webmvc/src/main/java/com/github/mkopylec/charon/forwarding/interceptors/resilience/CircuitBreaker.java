package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom;
import static io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry.of;
import static org.slf4j.LoggerFactory.getLogger;

class CircuitBreaker extends BasicCircuitBreaker implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(CircuitBreaker.class);

    CircuitBreaker() {
        // TODO Handle 5xx after https://github.com/resilience4j/resilience4j/issues/384 is done
        super(of(custom().build()));
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Circuit breaker for '{}' request mapping", execution.getMappingName());
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = registry.circuitBreaker(execution.getMappingName());
        setupMetrics(registry -> createMetrics(registry, execution.getMappingName()));
        HttpResponse response = circuitBreaker.executeSupplier(() -> execution.execute(request));
        log.trace("[End] Circuit breaker for '{}' request mapping", execution.getMappingName());
        return response;
    }
}
