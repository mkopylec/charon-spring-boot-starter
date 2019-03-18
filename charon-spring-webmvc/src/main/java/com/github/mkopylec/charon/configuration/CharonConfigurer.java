package com.github.mkopylec.charon.configuration;

public class CharonConfigurer {

    private CharonConfiguration charonConfiguration;

    private CharonConfigurer() {
        charonConfiguration = new CharonConfiguration();
    }

    public static CharonConfigurer charon() {
        return new CharonConfigurer();
    }

    public CharonConfigurer filterOrder(int filterOrder) {
        charonConfiguration.setFilterOrder(filterOrder);
        return this;
    }

    public CharonConfigurer configure(RequestPathRewriterConfigurer<?> requestPathRewriterConfigurer) {
        charonConfiguration.setRequestPathRewriter(requestPathRewriterConfigurer.getRequestPathRewriter());
        return this;
    }

    public CharonConfigurer configure(ResponseCookieRewriterConfigurer<?> responseCookieRewriterConfigurer) {
        charonConfiguration.setResponseCookieRewriter(responseCookieRewriterConfigurer.getResponseCookieRewriter());
        return this;
    }

    public CharonConfigurer configure(TimeoutConfigurer timeoutConfigurer) {
        charonConfiguration.setTimeoutConfiguration(timeoutConfigurer.getConfiguration());
        return this;
    }

    public CharonConfigurer configure(AsynchronousForwardingConfigurer asynchronousForwardingConfigurer) {
        charonConfiguration.setAsynchronousForwardingConfiguration(asynchronousForwardingConfigurer.getConfiguration());
        return this;
    }

    public CharonConfigurer configure(RetryConfigurer retryConfigurer) {
        charonConfiguration.setRetryConfiguration(retryConfigurer.getConfiguration());
        return this;
    }

    public CharonConfigurer configure(CircuitBreakerConfigurer circuitBreakerConfigurer) {
        charonConfiguration.setCircuitBreakerConfiguration(circuitBreakerConfigurer.getConfiguration());
        return this;
    }

    public CharonConfigurer configure(RateLimiterConfigurer rateLimiterConfigurer) {
        charonConfiguration.setRateLimiterConfiguration(rateLimiterConfigurer.getConfiguration());
        return this;
    }

    public CharonConfigurer add(RequestForwardingConfigurer requestForwardingConfigurer) {
        charonConfiguration.addRequestForwardingConfiguration(requestForwardingConfigurer.getConfiguration());
        return this;
    }

    CharonConfiguration getConfiguration() {
        return charonConfiguration;
    }
}
