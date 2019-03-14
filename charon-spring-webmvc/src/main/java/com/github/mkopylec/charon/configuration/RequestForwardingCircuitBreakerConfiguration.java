package com.github.mkopylec.charon.configuration;

public class RequestForwardingCircuitBreakerConfiguration {

    private boolean enabled;
    private CircuitBreakerConfiguration circuitBreakerConfiguration;

    RequestForwardingCircuitBreakerConfiguration() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public CircuitBreakerConfiguration getCircuitBreakerConfiguration() {
        return circuitBreakerConfiguration;
    }

    void setCircuitBreakerConfiguration(CircuitBreakerConfiguration circuitBreakerConfiguration) {
        this.circuitBreakerConfiguration = circuitBreakerConfiguration;
    }
}
