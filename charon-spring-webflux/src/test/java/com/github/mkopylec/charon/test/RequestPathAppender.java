package com.github.mkopylec.charon.test;

import java.net.URI;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import reactor.core.publisher.Mono;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;

import static org.springframework.web.util.UriComponentsBuilder.fromUri;

class RequestPathAppender implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        HttpRequest httpRequest = (HttpRequest) request;
        URI rewrittenUri = fromUri(httpRequest.url())
                .path("/appended")
                .build(true)
                .toUri();
        httpRequest.setUrl(rewrittenUri);
        return next.exchange(httpRequest);
    }
}
