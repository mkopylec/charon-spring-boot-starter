package com.github.mkopylec.charon.configuration;

import java.util.ArrayList;
import java.util.List;

import static com.github.mkopylec.charon.core.interceptors.RegexRequestPathRewriterConfigurer.regexRequestPathRewriter;
import static com.github.mkopylec.charon.core.interceptors.RootPathResponseCookieRewriterConfigurer.rootPathResponseCookieRewriter;
import static java.util.Collections.unmodifiableList;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

public class CharonConfiguration {

    private int filterOrder;
    // TODO Interceptor configurations list
    private RequestPathRewriter requestPathRewriter;
    private ResponseCookieRewriter responseCookieRewriter;
    private TimeoutConfiguration timeoutConfiguration;
    private AsynchronousForwardingConfiguration asynchronousForwardingConfiguration;
    private RetryConfiguration retryConfiguration;
    private CircuitBreakerConfiguration circuitBreakerConfiguration;
    private RateLimiterConfiguration rateLimiterConfiguration;
    private List<RequestForwardingConfiguration> requestForwardingConfigurations;

    CharonConfiguration() {
        filterOrder = LOWEST_PRECEDENCE;
        requestPathRewriter = ((ForwardingStartInterceptorConfigurer<?>) regexRequestPathRewriter()).getForwardingStartInterceptor();
        responseCookieRewriter = ((ForwardingCompleteInterceptorConfigurer<?>) rootPathResponseCookieRewriter()).getForwardingCompleteInterceptor();
        timeoutConfiguration = new TimeoutConfiguration();
        asynchronousForwardingConfiguration = new AsynchronousForwardingConfiguration();
        retryConfiguration = new RetryConfiguration();
        circuitBreakerConfiguration = new CircuitBreakerConfiguration();
        rateLimiterConfiguration = new RateLimiterConfiguration();
        requestForwardingConfigurations = new ArrayList<>();
        requestForwardingConfigurations.add(new RequestForwardingConfiguration("default"));
    }

    void validate() {
        timeoutConfiguration.validate();
        asynchronousForwardingConfiguration.validate();
        requestForwardingConfigurations.forEach(RequestForwardingConfiguration::validate);
    }

    public int getFilterOrder() {
        return filterOrder;
    }

    void setFilterOrder(int filterOrder) {
        this.filterOrder = filterOrder;
    }

    public RequestPathRewriter getRequestPathRewriter() {
        return requestPathRewriter;
    }

    void setRequestPathRewriter(RequestPathRewriter requestPathRewriter) {
        this.requestPathRewriter = requestPathRewriter;
    }

    public ResponseCookieRewriter getResponseCookieRewriter() {
        return responseCookieRewriter;
    }

    void setResponseCookieRewriter(ResponseCookieRewriter responseCookieRewriter) {
        this.responseCookieRewriter = responseCookieRewriter;
    }

    public TimeoutConfiguration getTimeoutConfiguration() {
        return timeoutConfiguration;
    }

    void setTimeoutConfiguration(TimeoutConfiguration timeoutConfiguration) {
        this.timeoutConfiguration = timeoutConfiguration;
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

    public List<RequestForwardingConfiguration> getRequestForwardingConfigurations() {
        return unmodifiableList(requestForwardingConfigurations);
    }

    void addRequestForwardingConfiguration(RequestForwardingConfiguration requestForwardingConfiguration) {
        requestForwardingConfigurations.add(requestForwardingConfiguration);
    }
}
