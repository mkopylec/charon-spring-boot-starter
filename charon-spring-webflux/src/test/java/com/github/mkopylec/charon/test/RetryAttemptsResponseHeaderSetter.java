package com.github.mkopylec.charon.test;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.String.valueOf;

class RetryAttemptsResponseHeaderSetter implements ExchangeFilterFunction {

    private AtomicInteger attempt = new AtomicInteger(); // Works only because of @DirtiesContext

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        // Exceptions thrown before return WILL NOT trigger circuit breaker nor retryer.
        return next.exchange(request)
                .map(response -> response.mutate()
                        .headers(httpHeaders -> httpHeaders.set("Retry-Attempts", valueOf(attempt.incrementAndGet())))
                        .build());
    }
}
