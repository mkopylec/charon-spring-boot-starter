package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.forwarding.WebClientConfigurer;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;

import java.util.function.Consumer;

import static org.springframework.util.Assert.notNull;

public class CharonConfigurer extends Configurer<CharonConfiguration> {

    private CharonConfigurer() {
        super(new CharonConfiguration());
    }

    public static CharonConfigurer charonConfiguration() {
        return new CharonConfigurer();
    }

    public CharonConfigurer filterOrder(int filterOrder) {
        configuredObject.setFilterOrder(filterOrder);
        return this;
    }

    public CharonConfigurer set(WebClientConfigurer webClientConfigurer) {
        configuredObject.setWebClientConfiguration(webClientConfigurer.configure());
        return this;
    }

    public CharonConfigurer set(RequestForwardingInterceptorConfigurer<?> requestForwardingInterceptorConfigurer) {
        configuredObject.addRequestForwardingInterceptor(requestForwardingInterceptorConfigurer.configure());
        return this;
    }

    public CharonConfigurer unset(RequestForwardingInterceptorType requestForwardingInterceptorType) {
        configuredObject.removeRequestForwardingInterceptor(requestForwardingInterceptorType);
        return this;
    }

    public CharonConfigurer add(RequestMappingConfigurer requestMappingConfigurer) {
        configuredObject.addRequestMappingConfiguration(requestMappingConfigurer.configure());
        return this;
    }

    public CharonConfigurer update(String requestMappingName, Consumer<RequestMappingConfigurer> requestMappingConfigurerUpdate) {
        RequestMappingConfigurer requestMappingConfigurer = configuredObject.getRequestMappingConfigurer(requestMappingName);
        notNull(requestMappingConfigurer, "Request mapping '" + requestMappingName + "' not found");
        requestMappingConfigurerUpdate.accept(requestMappingConfigurer);
        configuredObject.addRequestMappingConfiguration(requestMappingConfigurer.configure());
        return this;
    }

    @Override
    protected CharonConfiguration configure() {
        configuredObject.mergeWithRequestMappingConfigurations();
        return super.configure();
    }
}
