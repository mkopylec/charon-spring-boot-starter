package com.github.mkopylec.charon.configuration;

import java.util.List;

import static com.github.mkopylec.charon.utils.UriUtils.toUris;

public class RequestForwardingConfigurer {

    private RequestForwardingConfiguration requestForwardingConfiguration;

    private RequestForwardingConfigurer(String name) {
        requestForwardingConfiguration = new RequestForwardingConfiguration(name);
    }

    public static RequestForwardingConfigurer requestForwarding(String name) {
        return new RequestForwardingConfigurer(name);
    }

    public RequestForwardingConfigurer configure(RequestPathRewriterConfigurer<?> requestPathRewriterConfigurer) {
        requestForwardingConfiguration.setRequestPathRewriter(requestPathRewriterConfigurer.getRequestPathRewriter());
        return this;
    }

    public RequestForwardingConfigurer configure(ResponseCookieRewriterConfigurer<?> responseCookieRewriterConfigurer) {
        requestForwardingConfiguration.setResponseCookieRewriter(responseCookieRewriterConfigurer.getResponseCookieRewriter());
        return this;
    }

    public RequestForwardingConfigurer outgoingServers(String... outgoingServers) {
        requestForwardingConfiguration.setOutgoingServers(toUris(outgoingServers));
        return this;
    }

    public RequestForwardingConfigurer outgoingServers(List<String> outgoingServers) {
        requestForwardingConfiguration.setOutgoingServers(toUris(outgoingServers));
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

    public RequestForwardingConfigurer retryable(boolean retryable) {
        requestForwardingConfiguration.setRetryable(retryable);
        return this;
    }

    public RequestForwardingConfigurer configure(RetryConfigurer retryConfigurer) {
        requestForwardingConfiguration.setRetryConfiguration(retryConfigurer.getConfiguration());
        return this;
    }

    public RequestForwardingConfigurer circuitBreakable(boolean circuitBreakable) {
        requestForwardingConfiguration.setCircuitBreakable(circuitBreakable);
        return this;
    }

    public RequestForwardingConfigurer configure(CircuitBreakerConfigurer circuitBreakerConfigurer) {
        requestForwardingConfiguration.setCircuitBreakerConfiguration(circuitBreakerConfigurer.getConfiguration());
        return this;
    }

    public RequestForwardingConfigurer rateLimited(boolean rateLimited) {
        requestForwardingConfiguration.setRateLimited(rateLimited);
        return this;
    }

    public RequestForwardingConfigurer configure(RateLimiterConfigurer rateLimiterConfigurer) {
        requestForwardingConfiguration.setRateLimiterConfiguration(rateLimiterConfigurer.getConfiguration());
        return this;
    }

    public RequestForwardingConfigurer configure(CustomConfigurer customConfigurer) {
        requestForwardingConfiguration.setCustomConfiguration(customConfigurer.getConfiguration());
        return this;
    }

    RequestForwardingConfiguration getConfiguration() {
        requestForwardingConfiguration.validate();
        return requestForwardingConfiguration;
    }
}
