package com.github.mkopylec.charon.forwarding.interceptors;

import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

public class HttpRequestInterceptor implements ExchangeFilterFunction {

    private String mappingName;
    private RequestForwardingInterceptor requestForwardingInterceptor;

    public HttpRequestInterceptor(String mappingName, RequestForwardingInterceptor requestForwardingInterceptor) {
        this.mappingName = mappingName;
        this.requestForwardingInterceptor = requestForwardingInterceptor;
    }

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction exchange) {
        HttpRequest httpRequest = request instanceof HttpRequest
                ? (HttpRequest) request
                : new HttpRequest(request);
        HttpRequestExecution requestExecution = exchange instanceof HttpRequestExecution
                ? (HttpRequestExecution) exchange
                : new HttpRequestExecution(mappingName, exchange);
        return requestForwardingInterceptor.forward(httpRequest, requestExecution).cast(ClientResponse.class);
    }
}
