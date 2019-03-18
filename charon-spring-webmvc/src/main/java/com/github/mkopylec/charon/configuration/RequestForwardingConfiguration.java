package com.github.mkopylec.charon.configuration;

import java.net.URI;
import java.util.List;

import com.github.mkopylec.charon.core.RequestPathRewriter;
import com.github.mkopylec.charon.core.ResponseCookieRewriter;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;
import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.isTrue;

public class RequestForwardingConfiguration {

    private String name;
    private RequestPathRewriter requestPathRewriter;
    private ResponseCookieRewriter responseCookieRewriter;
    private List<URI> outgoingServers;
    private TimeoutConfiguration timeoutConfiguration;
    private boolean asynchronous;
    private boolean retryable;
    private RetryConfiguration retryConfiguration;
    private boolean circuitBreakable;
    private CircuitBreakerConfiguration circuitBreakerConfiguration;
    private boolean rateLimited;
    private RateLimiterConfiguration rateLimiterConfiguration;
    private CustomConfiguration customConfiguration;
    // TODO Interceptors configuration

    RequestForwardingConfiguration(String name) {
        this.name = name;
        outgoingServers = emptyList();
    }

    void validate() {
        hasText(name, "No request forwarding name set");
        isTrue(!outgoingServers.isEmpty(), "No outgoing servers set");
        timeoutConfiguration.validate();
    }

    public String getName() {
        return name;
    }

    public RequestPathRewriter getRequestPathRewriter() {
        return requestPathRewriter;
    }

    void setRequestPathRewriter(RequestPathRewriter requestPathRewriter) {
        this.requestPathRewriter = requestPathRewriter;
    }

    void mergeRequestPathRewriter(RequestPathRewriter requestPathRewriter) {
        if (this.requestPathRewriter == null) {
            this.requestPathRewriter = requestPathRewriter;
        }
    }

    public ResponseCookieRewriter getResponseCookieRewriter() {
        return responseCookieRewriter;
    }

    void setResponseCookieRewriter(ResponseCookieRewriter responseCookieRewriter) {
        this.responseCookieRewriter = responseCookieRewriter;
    }

    void mergeResponseCookieRewriter(ResponseCookieRewriter responseCookieRewriter) {
        if (this.responseCookieRewriter == null) {
            this.responseCookieRewriter = responseCookieRewriter;
        }
    }

    public List<URI> getOutgoingServers() {
        return outgoingServers;
    }

    void setOutgoingServers(List<URI> outgoingServers) {
        this.outgoingServers = emptyIfNull(outgoingServers);
    }

    public TimeoutConfiguration getTimeoutConfiguration() {
        return timeoutConfiguration;
    }

    void setTimeoutConfiguration(TimeoutConfiguration timeoutConfiguration) {
        this.timeoutConfiguration = timeoutConfiguration;
    }

    void mergeTimeoutConfiguration(TimeoutConfiguration timeoutConfiguration) {
        if (this.timeoutConfiguration == null) {
            this.timeoutConfiguration = timeoutConfiguration;
        }
    }

    public boolean isAsynchronous() {
        return asynchronous;
    }

    void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    public boolean isRetryable() {
        return retryable;
    }

    void setRetryable(boolean retryable) {
        this.retryable = retryable;
    }

    public RetryConfiguration getRetryConfiguration() {
        return retryConfiguration;
    }

    void setRetryConfiguration(RetryConfiguration retryConfiguration) {
        this.retryConfiguration = retryConfiguration;
    }

    void mergeRetryConfiguration(RetryConfiguration retryConfiguration) {
        if (this.retryConfiguration == null) {
            this.retryConfiguration = retryConfiguration;
        }
    }

    public boolean isCircuitBreakable() {
        return circuitBreakable;
    }

    void setCircuitBreakable(boolean circuitBreakable) {
        this.circuitBreakable = circuitBreakable;
    }

    public CircuitBreakerConfiguration getCircuitBreakerConfiguration() {
        return circuitBreakerConfiguration;
    }

    void setCircuitBreakerConfiguration(CircuitBreakerConfiguration circuitBreakerConfiguration) {
        this.circuitBreakerConfiguration = circuitBreakerConfiguration;
    }

    void mergeCircuitBreakerConfiguration(CircuitBreakerConfiguration circuitBreakerConfiguration) {
        if (this.circuitBreakerConfiguration == null) {
            this.circuitBreakerConfiguration = circuitBreakerConfiguration;
        }
    }

    public boolean isRateLimited() {
        return rateLimited;
    }

    void setRateLimited(boolean rateLimited) {
        this.rateLimited = rateLimited;
    }

    public RateLimiterConfiguration getRateLimiterConfiguration() {
        return rateLimiterConfiguration;
    }

    void setRateLimiterConfiguration(RateLimiterConfiguration rateLimiterConfiguration) {
        this.rateLimiterConfiguration = rateLimiterConfiguration;
    }

    void mergeRateLimiterConfiguration(RateLimiterConfiguration rateLimiterConfiguration) {
        if (this.rateLimiterConfiguration == null) {
            this.rateLimiterConfiguration = rateLimiterConfiguration;
        }
    }

    public CustomConfiguration getCustomConfiguration() {
        return customConfiguration;
    }

    void setCustomConfiguration(CustomConfiguration customConfiguration) {
        this.customConfiguration = customConfiguration;
    }
}
