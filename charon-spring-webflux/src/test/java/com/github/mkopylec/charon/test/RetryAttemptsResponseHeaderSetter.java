package com.github.mkopylec.charon.test;

import java.util.concurrent.atomic.AtomicInteger;

import reactor.core.publisher.Mono;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;

import static java.lang.String.valueOf;
import static org.springframework.web.reactive.function.BodyExtractors.toDataBuffers;
import static org.springframework.web.reactive.function.client.ClientResponse.from;

class RetryAttemptsResponseHeaderSetter implements ExchangeFilterFunction {

    private AtomicInteger attempt = new AtomicInteger(); // Works only because of @DirtiesContext

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        // Exceptions thrown before return WILL NOT trigger circuit breaker nor retryer.
        return next.exchange(request)
                .map(response -> from(response)
                        .headers(httpHeaders -> httpHeaders.set("Retry-Attempts", valueOf(attempt.incrementAndGet())))
                        .body(response.body(toDataBuffers())) // Need to consume body, otherwise the request will HANG after ReverseProxyFilter.
                        .build());
    }
}
