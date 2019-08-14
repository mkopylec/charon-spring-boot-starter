package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.RequestMappingConfiguration;

import java.util.List;

public interface RequestMappingProvider {

    List<RequestMappingConfiguration> getRequestMappingConfigurations();
}
