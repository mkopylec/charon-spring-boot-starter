package com.github.mkopylec.charon.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.HttpRequest;
import com.github.mkopylec.charon.forwarding.HttpResponse;
import com.github.mkopylec.charon.forwarding.RequestForwarder;
import com.github.mkopylec.charon.forwarding.RequestForwarding;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;

import static io.github.resilience4j.ratelimiter.RateLimiterConfig.custom;

class RateLimitingHandler extends ResilienceHandler<RateLimiterConfig> {

    RateLimitingHandler() {
        super(custom().build());
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
}
