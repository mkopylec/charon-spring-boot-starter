package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.Configurer;

import org.springframework.boot.web.client.RestTemplateBuilder;

public abstract class RestTemplateConfigurer<C extends RestTemplateConfigurer<C>> extends Configurer<RestTemplateConfiguration> {

    protected RestTemplateConfigurer(RestTemplateConfiguration configuredObject) {
        super(configuredObject);
    }

    @SuppressWarnings("unchecked")
    public C configuration(RestTemplateBuilder restTemplateBuilder) {
        configuredObject.setConfiguration(restTemplateBuilder);
        return (C) this;
    }
}
