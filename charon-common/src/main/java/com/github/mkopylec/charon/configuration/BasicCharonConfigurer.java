package com.github.mkopylec.charon.configuration;

import java.util.function.Consumer;

import com.github.mkopylec.charon.forwarding.BasicRequestForwardingInterceptorConfigurer;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;

import static org.springframework.util.Assert.notNull;

public abstract class BasicCharonConfigurer<T extends BasicCharonConfiguration, C extends BasicCharonConfigurer<T, C>> extends Configurer<T> {

    BasicCharonConfigurer(T configuredObject) {
        super(configuredObject);
    }

    public C filterOrder(int filterOrder) {
        configuredObject.setFilterOrder(filterOrder);
        return (C) this;
    }

    public C set(BasicRequestForwardingInterceptorConfigurer<?> requestForwardingInterceptorConfigurer) {
        configuredObject.addRequestForwardingInterceptor(requestForwardingInterceptorConfigurer.configure());
        return (C) this;
    }

    public C unset(RequestForwardingInterceptorType requestForwardingInterceptorType) {
        configuredObject.removeRequestForwardingInterceptor(requestForwardingInterceptorType);
        return (C) this;
    }

    public C add(BasicRequestMappingConfigurer<?, ?> requestMappingConfigurer) {
        configuredObject.addRequestMappingConfiguration(requestMappingConfigurer.configure());
        return (C) this;
    }

    // TODO BasicRequestMappingConfigurer must be seen as concrete subclass
    public C update(String requestMappingName, Consumer<BasicRequestMappingConfigurer<?, ?>> requestMappingConfigurerUpdate) {
        BasicRequestMappingConfigurer<?, ?> requestMappingConfigurer = configuredObject.getRequestMappingConfigurer(requestMappingName);
        notNull(requestMappingConfigurer, "Request mapping '" + requestMappingName + "' not found");
        requestMappingConfigurerUpdate.accept(requestMappingConfigurer);
        configuredObject.addRequestMappingConfiguration(requestMappingConfigurer.configure());
        return (C) this;
    }

    @Override
    protected T configure() {
        configuredObject.mergeWithRequestMappingConfigurations();
        return super.configure();
    }
}
