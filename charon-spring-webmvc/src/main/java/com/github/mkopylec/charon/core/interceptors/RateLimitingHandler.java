package com.github.mkopylec.charon.core.interceptors;

import com.github.mkopylec.charon.core.HttpRequest;
import com.github.mkopylec.charon.core.HttpResponse;
import com.github.mkopylec.charon.core.RequestForwarder;
import com.github.mkopylec.charon.core.RequestForwarding;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;

import static io.github.resilience4j.ratelimiter.RateLimiterConfig.custom;

public class RateLimitingHandler implements RequestForwardingInterceptor {

    protected boolean enabled;
    protected RateLimiterConfig rateLimiterConfig;
    protected boolean measured;

    public RateLimitingHandler() {
        enabled = true;
        rateLimiterConfig = custom().build();
    }

    @Override
    public HttpResponse forward(HttpRequest request, RequestForwarding forwarding, RequestForwarder forwarder) {
        // TODO Implement rate limiting
        return null;
    }

    @Override
    public int getOrder() {
        return RATE_LIMITING_HANDLER_ORDER;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setRateLimiterConfig(RateLimiterConfig rateLimiterConfig) {
        this.rateLimiterConfig = rateLimiterConfig;
    }

    public void setMeasured(boolean measured) {
        this.measured = measured;
    }
}
