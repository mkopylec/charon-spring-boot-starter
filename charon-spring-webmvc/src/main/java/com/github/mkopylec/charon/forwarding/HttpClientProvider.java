package com.github.mkopylec.charon.forwarding;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.web.client.RestTemplate;

class HttpClientProvider {

    private HttpClientFactory httpClientFactory;
    private ConcurrentMap<String, RestTemplate> httpClients;

    HttpClientProvider(HttpClientFactory httpClientFactory) {
        this.httpClientFactory = httpClientFactory;
        httpClients = new ConcurrentHashMap<>();
    }

    RestTemplate getHttpClient(RequestForwarding forwarding) {
        return httpClients.computeIfAbsent(forwarding.getName(), forwardingName -> httpClientFactory.createHttpClient(forwarding));
    }
}
