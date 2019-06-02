package com.github.mkopylec.charon.forwarding.interceptors;

import com.github.mkopylec.charon.forwarding.CustomConfiguration;
import reactor.core.publisher.Mono;

import org.springframework.core.Ordered;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;

public class HttpRequestInterceptor implements ExchangeFilterFunction, Ordered {

    private String mappingName;
    private CustomConfiguration customConfiguration;
    private RequestForwardingInterceptor requestForwardingInterceptor;

    public HttpRequestInterceptor(String mappingName, CustomConfiguration customConfiguration, RequestForwardingInterceptor requestForwardingInterceptor) {
        this.mappingName = mappingName;
        this.customConfiguration = customConfiguration;
        this.requestForwardingInterceptor = requestForwardingInterceptor;
    }

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction exchange) {
        HttpRequest httpRequest = request instanceof HttpRequest
                ? (HttpRequest) request
                : new HttpRequest(request);
        HttpRequestExecution requestExecution = exchange instanceof HttpRequestExecution
                ? (HttpRequestExecution) exchange
                : new HttpRequestExecution(mappingName, customConfiguration, exchange);
        return requestForwardingInterceptor.forward(httpRequest, requestExecution)
                .map(httpResponse -> httpResponse);
    }

    @Override
    public int getOrder() {
        return requestForwardingInterceptor.getOrder();
    }
}
