package com.github.mkopylec.charon.configuration;

public class RequestForwardingConfigurer {

    private RequestForwardingConfiguration requestForwardingConfiguration;

    private RequestForwardingConfigurer(String name) {
        requestForwardingConfiguration = new RequestForwardingConfiguration(name);
    }

    public static RequestForwardingConfigurer requestForwarding(String name) {
        return new RequestForwardingConfigurer(name);
    }

    public RequestForwardingConfigurer set(RequestForwardingInterceptorConfigurer<?> requestForwardingInterceptorConfigurer) {
        requestForwardingConfiguration.addRequestForwardingInterceptor(requestForwardingInterceptorConfigurer.getRequestForwardingInterceptor());
        return this;
    }

    public RequestForwardingConfigurer set(TimeoutConfigurer timeoutConfigurer) {
        requestForwardingConfiguration.setTimeoutConfiguration(timeoutConfigurer.getConfiguration());
        return this;
    }

    public RequestForwardingConfigurer set(CustomConfigurer customConfigurer) {
        requestForwardingConfiguration.setCustomConfiguration(customConfigurer.getConfiguration());
        return this;
    }

    RequestForwardingConfiguration getConfiguration() {
        requestForwardingConfiguration.validate();
        return requestForwardingConfiguration;
    }
}
