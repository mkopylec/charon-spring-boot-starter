package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.RequestMappingConfiguration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class WebClientProvider {

    private ConcurrentMap<String, WebClient> restTemplates;

    WebClientProvider() {
        restTemplates = new ConcurrentHashMap<>();
    }

    WebClient getWebClient(RequestMappingConfiguration configuration) {
        return restTemplates.computeIfAbsent(configuration.getName(), mappingName -> configuration.getWebClientConfiguration().configure(configuration));
    }
}
