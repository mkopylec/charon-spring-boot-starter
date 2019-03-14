package com.github.mkopylec.charon.configuration;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

public class CharonConfiguration {

    private int filterOrder;
    private AsynchronousForwardingConfiguration asynchronousForwardingConfiguration;
    private RetryConfiguration retryConfiguration;
    private CircuitBreakerConfiguration circuitBreakerConfiguration;
    private RateLimiterConfiguration rateLimiterConfiguration;
    private List<RequestForwardingConfiguration> requestForwardingConfigurations;

    CharonConfiguration() {
        filterOrder = LOWEST_PRECEDENCE;
        asynchronousForwardingConfiguration = new AsynchronousForwardingConfiguration();
        retryConfiguration = new RetryConfiguration();
        circuitBreakerConfiguration = new CircuitBreakerConfiguration();
        rateLimiterConfiguration = new RateLimiterConfiguration();
        requestForwardingConfigurations = singletonList(new RequestForwardingConfiguration("default"));
    }

    public int getFilterOrder() {
        return filterOrder;
    }

    void setFilterOrder(int filterOrder) {
        this.filterOrder = filterOrder;
    }

    public AsynchronousForwardingConfiguration getAsynchronousForwardingConfiguration() {
        return asynchronousForwardingConfiguration;
    }

    void setAsynchronousForwardingConfiguration(AsynchronousForwardingConfiguration asynchronousForwardingConfiguration) {
        this.asynchronousForwardingConfiguration = asynchronousForwardingConfiguration;
    }

    public RetryConfiguration getRetryConfiguration() {
        return retryConfiguration;
    }

    void setRetryConfiguration(RetryConfiguration retryConfiguration) {
        this.retryConfiguration = retryConfiguration;
    }

    public CircuitBreakerConfiguration getCircuitBreakerConfiguration() {
        return circuitBreakerConfiguration;
    }

    void setCircuitBreakerConfiguration(CircuitBreakerConfiguration circuitBreakerConfiguration) {
        this.circuitBreakerConfiguration = circuitBreakerConfiguration;
    }

    public RateLimiterConfiguration getRateLimiterConfiguration() {
        return rateLimiterConfiguration;
    }

    void setRateLimiterConfiguration(RateLimiterConfiguration rateLimiterConfiguration) {
        this.rateLimiterConfiguration = rateLimiterConfiguration;
    }
}
