package com.github.mkopylec.charon.forwarding.interceptors;

import io.netty.channel.ChannelException;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import static com.github.mkopylec.charon.forwarding.RequestForwardingException.requestForwardingError;

public class HttpRequestExecution {

    private String mappingName;
    private ExchangeFunction exchange;

    HttpRequestExecution(String mappingName, ExchangeFunction exchange) {
        this.mappingName = mappingName;
        this.exchange = exchange;
    }

    public Mono<HttpResponse> execute(HttpRequest request) {
        return exchange.exchange(request)
                .map(response -> response instanceof HttpResponse
                        ? (HttpResponse) response
                        : new HttpResponse(response))
                .doOnError(ChannelException.class, e -> {
                    throw requestForwardingError("Error executing request: " + e.getMessage(), e);
                });
    }

    public String getMappingName() {
        return mappingName;
    }
}
