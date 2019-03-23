package com.github.mkopylec.charon.configuration;

public class CharonConfigurer {

    private CharonConfiguration charonConfiguration;

    private CharonConfigurer() {
        charonConfiguration = new CharonConfiguration();
    }

    public static CharonConfigurer charonConfiguration() {
        return new CharonConfigurer();
    }

    public CharonConfigurer filterOrder(int filterOrder) {
        charonConfiguration.setFilterOrder(filterOrder);
        return this;
    }

    public CharonConfigurer set(TimeoutConfigurer timeoutConfigurer) {
        charonConfiguration.setTimeoutConfiguration(timeoutConfigurer.getConfiguration());
        return this;
    }

    public CharonConfigurer set(RequestForwardingInterceptorConfigurer<?> requestForwardingInterceptorConfigurer) {
        charonConfiguration.addRequestForwardingInterceptor(requestForwardingInterceptorConfigurer.getRequestForwardingInterceptor());
        return this;
    }

    public CharonConfigurer add(RequestForwardingConfigurer requestForwardingConfigurer) {
        charonConfiguration.addRequestForwardingConfiguration(requestForwardingConfigurer.getConfiguration());
        return this;
    }

    public CharonConfigurer set(CustomConfigurer customConfigurer) {
        charonConfiguration.setCustomConfiguration(customConfigurer.getConfiguration());
        return this;
    }

    CharonConfiguration getConfiguration() {
        return charonConfiguration;
    }
}
