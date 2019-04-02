package com.github.mkopylec.charon.forwarding;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.github.mkopylec.charon.configuration.RequestForwardingConfiguration;

import org.springframework.web.client.RestTemplate;

class RestTemplateProvider {

    private RestTemplateConfiguration restTemplateConfiguration;
    private ConcurrentMap<String, RestTemplate> restTemplates;

    RestTemplateProvider(RestTemplateConfiguration restTemplateConfiguration) {
        this.restTemplateConfiguration = restTemplateConfiguration;
        restTemplates = new ConcurrentHashMap<>();
    }

    RestTemplate getRestTemplate(RequestForwardingConfiguration configuration) {
        return restTemplates.computeIfAbsent(configuration.getName(), forwardingName -> restTemplateConfiguration.configure(configuration));
    }
}
