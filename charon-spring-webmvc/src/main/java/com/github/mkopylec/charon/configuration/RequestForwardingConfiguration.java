package com.github.mkopylec.charon.configuration;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

public class RequestForwardingConfiguration {

    private String name;
    private String incomingPathAntPattern;
    private PathRewriteConfiguration pathRewriteConfiguration;
    private List<String> outgoingHosts;
    private TimeoutConfiguration timeoutConfiguration;
    private boolean asynchronous;
    private RequestForwardingRetryConfiguration requestForwardingRetryConfiguration;
    private RequestForwardingCircuitBreakerConfiguration requestForwardingCircuitBreakerConfiguration;
    private RequestForwardingRateLimiterConfiguration requestForwardingRateLimiterConfiguration;
    private Map<String, Object> customConfiguration;

    RequestForwardingConfiguration(String name) {
        this.name = name;
        incomingPathAntPattern = "/**";
        pathRewriteConfiguration = new PathRewriteConfiguration();
        outgoingHosts = emptyList();
        timeoutConfiguration = new TimeoutConfiguration();
        requestForwardingRetryConfiguration = new RequestForwardingRetryConfiguration();
        requestForwardingCircuitBreakerConfiguration = new RequestForwardingCircuitBreakerConfiguration();
        requestForwardingRateLimiterConfiguration = new RequestForwardingRateLimiterConfiguration();
        customConfiguration = emptyMap();
    }

    public String getName() {
        return name;
    }

    public String getIncomingPathAntPattern() {
        return incomingPathAntPattern;
    }

    void setIncomingPathAntPattern(String incomingPathAntPattern) {
        this.incomingPathAntPattern = incomingPathAntPattern;
    }

    public PathRewriteConfiguration getPathRewriteConfiguration() {
        return pathRewriteConfiguration;
    }

    void setPathRewriteConfiguration(PathRewriteConfiguration pathRewriteConfiguration) {
        this.pathRewriteConfiguration = pathRewriteConfiguration;
    }

    public List<String> getOutgoingHosts() {
        return outgoingHosts;
    }

    void setOutgoingHosts(List<String> outgoingHosts) {
        this.outgoingHosts = unmodifiableList(outgoingHosts);
    }

    public TimeoutConfiguration getTimeoutConfiguration() {
        return timeoutConfiguration;
    }

    void setTimeoutConfiguration(TimeoutConfiguration timeoutConfiguration) {
        this.timeoutConfiguration = timeoutConfiguration;
    }

    public boolean isAsynchronous() {
        return asynchronous;
    }

    void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    public RequestForwardingRetryConfiguration getRequestForwardingRetryConfiguration() {
        return requestForwardingRetryConfiguration;
    }

    void setRequestForwardingRetryConfiguration(RequestForwardingRetryConfiguration requestForwardingRetryConfiguration) {
        this.requestForwardingRetryConfiguration = requestForwardingRetryConfiguration;
    }

    public RequestForwardingCircuitBreakerConfiguration getRequestForwardingCircuitBreakerConfiguration() {
        return requestForwardingCircuitBreakerConfiguration;
    }

    void setRequestForwardingCircuitBreakerConfiguration(RequestForwardingCircuitBreakerConfiguration requestForwardingCircuitBreakerConfiguration) {
        this.requestForwardingCircuitBreakerConfiguration = requestForwardingCircuitBreakerConfiguration;
    }

    public RequestForwardingRateLimiterConfiguration getRequestForwardingRateLimiterConfiguration() {
        return requestForwardingRateLimiterConfiguration;
    }

    void setRequestForwardingRateLimiterConfiguration(RequestForwardingRateLimiterConfiguration requestForwardingRateLimiterConfiguration) {
        this.requestForwardingRateLimiterConfiguration = requestForwardingRateLimiterConfiguration;
    }

    public Map<String, Object> getCustomConfiguration() {
        return customConfiguration;
    }

    void setCustomConfiguration(Map<String, Object> customConfiguration) {
        this.customConfiguration = unmodifiableMap(customConfiguration);
    }
}
