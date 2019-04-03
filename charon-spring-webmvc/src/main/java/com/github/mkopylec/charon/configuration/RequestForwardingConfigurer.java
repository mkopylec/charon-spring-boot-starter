package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.forwarding.CustomConfigurer;
import com.github.mkopylec.charon.forwarding.RestTemplateConfigurer;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptorConfigurer;

public class RequestForwardingConfigurer extends Configurer<RequestForwardingConfiguration> {

    private RequestForwardingConfigurer(String name) {
        super(new RequestForwardingConfiguration(name));
    }

    public static RequestForwardingConfigurer requestForwarding(String name) {
        return new RequestForwardingConfigurer(name);
    }

    public RequestForwardingConfigurer pathRegex(String pathRegex) {
        configuredObject.setPathRegex(pathRegex);
        return this;
    }

    public RequestForwardingConfigurer set(RestTemplateConfigurer restTemplateConfigurer) {
        configuredObject.setRestTemplateConfiguration(restTemplateConfigurer.configure());
        return this;
    }

    public RequestForwardingConfigurer set(RequestForwardingInterceptorConfigurer<?> requestForwardingInterceptorConfigurer) {
        configuredObject.addRequestForwardingInterceptor(requestForwardingInterceptorConfigurer.configure());
        return this;
    }

    public RequestForwardingConfigurer set(CustomConfigurer customConfigurer) {
        configuredObject.setCustomConfiguration(customConfigurer.configure());
        return this;
    }
}
