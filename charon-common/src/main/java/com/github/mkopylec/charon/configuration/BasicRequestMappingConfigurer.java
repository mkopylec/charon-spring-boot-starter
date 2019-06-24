package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.forwarding.BasicRequestForwardingInterceptorConfigurer;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;

public abstract class BasicRequestMappingConfigurer<M extends BasicRequestMappingConfiguration, C extends BasicRequestMappingConfigurer<M, C>> extends Configurer<M> {

    BasicRequestMappingConfigurer(M configuredObject) {
        super(configuredObject);
    }

    @SuppressWarnings("unchecked")
    public C pathRegex(String pathRegex) {
        configuredObject.setPathRegex(pathRegex);
        return (C) this;
    }

    @SuppressWarnings("unchecked")
    public C set(BasicRequestForwardingInterceptorConfigurer<?> requestForwardingInterceptorConfigurer) {
        configuredObject.addRequestForwardingInterceptor(requestForwardingInterceptorConfigurer.configure());
        return (C) this;
    }

    @SuppressWarnings("unchecked")
    public C unset(RequestForwardingInterceptorType requestForwardingInterceptorType) {
        configuredObject.removeRequestForwardingInterceptor(requestForwardingInterceptorType);
        return (C) this;
    }

    @Override
    protected M configure() {
        configuredObject.setRequestMappingConfigurer(this);
        return super.configure();
    }
}
