package com.github.mkopylec.charon.forwarding.interceptors;

import com.github.mkopylec.charon.forwarding.CustomConfiguration;
import reactor.core.publisher.Mono;

import org.springframework.web.reactive.function.client.ExchangeFunction;

public class HttpRequestExecution {

    private String mappingName;
    private CustomConfiguration customConfiguration;
    private ExchangeFunction exchange;

    HttpRequestExecution(String mappingName, CustomConfiguration customConfiguration, ExchangeFunction exchange) {
        this.mappingName = mappingName;
        this.customConfiguration = customConfiguration;
        this.exchange = exchange;
    }

    public Mono<HttpResponse> execute(HttpRequest request) {
        return exchange.exchange(request)
                .map(response -> response instanceof HttpResponse
                        ? (HttpResponse) response
                        : new HttpResponse(response));
    }

    public String getMappingName() {
        return mappingName;
    }

    public <P> P getCustomProperty(String name) {
        return customConfiguration.getProperty(name);
    }
}
