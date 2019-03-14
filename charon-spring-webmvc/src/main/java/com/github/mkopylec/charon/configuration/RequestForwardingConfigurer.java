package com.github.mkopylec.charon.configuration;

import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

public class RequestForwardingConfigurer {

    private RequestForwardingConfiguration requestForwardingConfiguration;

    private RequestForwardingConfigurer(String name) {
        requestForwardingConfiguration = new RequestForwardingConfiguration(name);
    }

    public static RequestForwardingConfigurer requestForwarding(String name) {
        return new RequestForwardingConfigurer(name);
    }

    public RequestForwardingConfigurer incomingPathAntPattern(String incomingPathAntPattern) {
        requestForwardingConfiguration.setIncomingPathAntPattern(incomingPathAntPattern);
        return this;
    }

    public RequestForwardingConfigurer configure(PathRewriteConfigurer pathRewriteConfigurer) {
        requestForwardingConfiguration.setPathRewriteConfiguration(pathRewriteConfigurer.getConfiguration());
        return this;
    }

    public RequestForwardingConfigurer outgoingHosts(String... outgoingHosts) {
        requestForwardingConfiguration.setOutgoingHosts(asList(outgoingHosts));
        return this;
    }

    public RequestForwardingConfigurer outgoingHosts(List<String> outgoingHosts) {
        requestForwardingConfiguration.setOutgoingHosts(outgoingHosts);
        return this;
    }

    public RequestForwardingConfigurer configure(TimeoutConfigurer timeoutConfigurer) {
        requestForwardingConfiguration.setTimeoutConfiguration(timeoutConfigurer.getConfiguration());
        return this;
    }

    public RequestForwardingConfigurer asynchronous(boolean asynchronous) {
        requestForwardingConfiguration.setAsynchronous(asynchronous);
        return this;
    }

    public RequestForwardingConfigurer configure(RequestForwardingRetryConfigurer requestForwardingRetryConfigurer) {
        requestForwardingConfiguration.setRequestForwardingRetryConfiguration(requestForwardingRetryConfigurer.getConfiguration());
        return this;
    }

    public RequestForwardingConfigurer configure(RequestForwardingCircuitBreakerConfigurer requestForwardingCircuitBreakerConfigurer) {
        requestForwardingConfiguration.setRequestForwardingCircuitBreakerConfiguration(requestForwardingCircuitBreakerConfigurer.getConfiguration());
        return this;
    }

    public RequestForwardingConfigurer configure(RequestForwardingRateLimiterConfigurer requestForwardingRateLimiterConfigurer) {
        requestForwardingConfiguration.setRequestForwardingRateLimiterConfiguration(requestForwardingRateLimiterConfigurer.getConfiguration());
        return this;
    }

    public RequestForwardingConfigurer customConfiguration(Map<String, Object> customConfiguration) {
        requestForwardingConfiguration.setCustomConfiguration(customConfiguration);
        return this;
    }

    RequestForwardingConfiguration getConfiguration() {
        return requestForwardingConfiguration;
    }
}
