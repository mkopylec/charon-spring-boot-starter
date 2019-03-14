package com.github.mkopylec.charon.configuration;

public class RequestForwardingCircuitBreakerConfigurer {

    private RequestForwardingCircuitBreakerConfiguration requestForwardingCircuitBreakerConfiguration;

    private RequestForwardingCircuitBreakerConfigurer() {
        requestForwardingCircuitBreakerConfiguration = new RequestForwardingCircuitBreakerConfiguration();
    }

    public static RequestForwardingCircuitBreakerConfigurer requestForwardingCircuitBreaker() {
        return new RequestForwardingCircuitBreakerConfigurer();
    }

    public RequestForwardingCircuitBreakerConfigurer enabled(boolean enabled) {
        requestForwardingCircuitBreakerConfiguration.setEnabled(enabled);
        return this;
    }

    public RequestForwardingCircuitBreakerConfigurer configure(CircuitBreakerConfigurer circuitBreakerConfigurer) {
        requestForwardingCircuitBreakerConfiguration.setCircuitBreakerConfiguration(circuitBreakerConfigurer.getConfiguration());
        return this;
    }

    RequestForwardingCircuitBreakerConfiguration getConfiguration() {
        return requestForwardingCircuitBreakerConfiguration;
    }
}
