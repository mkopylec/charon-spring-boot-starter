package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.configuration.RequestMappingConfiguration;
import com.github.mkopylec.charon.forwarding.RequestMappingProvider;

import java.util.List;

class StaticRequestMappingProvider implements RequestMappingProvider {

    private List<RequestMappingConfiguration> requestMappingConfigurations;

    StaticRequestMappingProvider(List<RequestMappingConfiguration> requestMappingConfigurations) {
        this.requestMappingConfigurations = requestMappingConfigurations;
    }

    @Override
    public List<RequestMappingConfiguration> getRequestMappingConfigurations() {
        return requestMappingConfigurations;
    }
}
