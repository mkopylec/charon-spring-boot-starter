package com.github.mkopylec.charon.forwarding;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.github.mkopylec.charon.configuration.RequestForwardingConfiguration;

import org.springframework.web.client.RestTemplate;

class RestTemplateProvider {

    private ConcurrentMap<String, RestTemplate> restTemplates;

    RestTemplateProvider() {
        restTemplates = new ConcurrentHashMap<>();
    }

    RestTemplate getRestTemplate(RequestForwardingConfiguration configuration) {
        return restTemplates.computeIfAbsent(configuration.getName(), forwardingName -> configuration.getRestTemplateConfiguration().configure(configuration));
    }
}
