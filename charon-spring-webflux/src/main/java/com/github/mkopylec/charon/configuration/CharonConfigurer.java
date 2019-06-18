package com.github.mkopylec.charon.configuration;

import java.util.function.Consumer;

import com.github.mkopylec.charon.forwarding.WebClientConfigurer;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;

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

    public <C extends RequestForwardingInterceptorConfigurer<?>> CharonConfigurer update(C requestForwardingInterceptorConfigurer, Consumer<C> requestForwardingInterceptorConfigurerUpdate) {
        // TODO Should allow different configs for different profiles. Implement update for every Configurer. Should updates be invoked in merge phase or right away?
        return this;
    }

    public CharonConfigurer unset(RequestForwardingInterceptorType requestForwardingInterceptorType) {
        configuredObject.removeRequestForwardingInterceptor(requestForwardingInterceptorType);
        return this;
    }

    public CharonConfigurer add(RequestMappingConfigurer requestMappingConfigurer) {
        configuredObject.addRequestForwardingConfiguration(requestMappingConfigurer.configure());
        return this;
    }

    @Override
    protected CharonConfiguration configure() {
        configuredObject.mergeWithRequestForwardingConfigurations();
        return super.configure();
    }
}
