package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.forwarding.CustomConfigurer;
import com.github.mkopylec.charon.forwarding.RestTemplateConfigurer;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

public class RequestMappingConfigurer extends Configurer<RequestMappingConfiguration> {

    private RequestMappingConfigurer(String name) {
        super(new RequestMappingConfiguration(name));
    }

    public static RequestMappingConfigurer requestMapping(String name) {
        return new RequestMappingConfigurer(name);
    }

    public RequestMappingConfigurer pathRegex(String pathRegex) {
        configuredObject.setPathRegex(pathRegex);
        return this;
    }

    public RequestMappingConfigurer set(RestTemplateConfigurer restTemplateConfigurer) {
        configuredObject.setRestTemplateConfiguration(restTemplateConfigurer.configure());
        return this;
    }

    public RequestMappingConfigurer set(RequestForwardingInterceptorConfigurer<?> requestForwardingInterceptorConfigurer) {
        configuredObject.addRequestForwardingInterceptor(requestForwardingInterceptorConfigurer.configure());
        return this;
    }

    public RequestMappingConfigurer set(CustomConfigurer customConfigurer) {
        configuredObject.setCustomConfiguration(customConfigurer.configure());
        return this;
    }
}
