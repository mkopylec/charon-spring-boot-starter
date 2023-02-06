package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.RequestMappingConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class RestTemplateProvider {

    private ConcurrentMap<String, RestTemplate> restTemplates;

    RestTemplateProvider() {
        restTemplates = new ConcurrentHashMap<>();
    }

    RestTemplate getRestTemplate(RequestMappingConfiguration configuration) {
        return restTemplates.computeIfAbsent(configuration.getName(), mappingName -> configuration.getRestTemplateConfiguration().configure(configuration));
    }
}
