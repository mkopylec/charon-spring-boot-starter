package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptorConfigurer;

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

    public CharonConfigurer set(TimeoutConfigurer timeoutConfigurer) {
        configuredObject.setTimeoutConfiguration(timeoutConfigurer.configure());
        return this;
    }

    public CharonConfigurer set(RequestForwardingInterceptorConfigurer<?> requestForwardingInterceptorConfigurer) {
        configuredObject.addRequestForwardingInterceptor(requestForwardingInterceptorConfigurer.configure());
        return this;
    }

    public CharonConfigurer add(RequestForwardingConfigurer requestForwardingConfigurer) {
        configuredObject.addRequestForwardingConfiguration(requestForwardingConfigurer.configure());
        return this;
    }

    public CharonConfigurer set(CustomConfigurer customConfigurer) {
        configuredObject.setCustomConfiguration(customConfigurer.configure());
        return this;
    }
}
