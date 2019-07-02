package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import static io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator.of;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.just;

class CircuitBreaker extends CommonCircuitBreaker<HttpResponse> implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(CircuitBreaker.class);

    CircuitBreaker() {
        super(log);
    }

    @Override
    public Mono<HttpResponse> forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker = getRegistry().circuitBreaker(execution.getMappingName());
        setupMetrics(registry -> createMetrics(registry, execution.getMappingName()));
        return execution.execute(request)
                .transform(of(circuitBreaker))
                .onErrorResume(CallNotPermittedException.class, ex -> just(executeFallback(ex)))
                .doOnSuccess(response -> logEnd(execution.getMappingName()));
    }
}
