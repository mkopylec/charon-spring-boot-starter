package com.github.mkopylec.charon.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom;

public class CircuitBreakerConfiguration {

    private CircuitBreakerConfig circuitBreakerConfig;
    private boolean metricsEnabled;

    CircuitBreakerConfiguration() {
        circuitBreakerConfig = custom().build();
    }

    // TODO Create r4j registries, maybe not here, they are needed for distinction of mappings configs and for metrics.
    public CircuitBreakerConfig getCircuitBreakerConfig() {
        return circuitBreakerConfig;
    }

    void setCircuitBreakerConfig(CircuitBreakerConfig circuitBreakerConfig) {
        this.circuitBreakerConfig = circuitBreakerConfig;
    }

    public boolean isMetricsEnabled() {
        return metricsEnabled;
    }

    void setMetricsEnabled(boolean metricsEnabled) {
        this.metricsEnabled = metricsEnabled;
    }
}
