package com.github.mkopylec.charon.interceptors.resilience;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;

import static io.github.resilience4j.ratelimiter.RateLimiterConfig.custom;

class RateLimitingHandler extends ResilienceHandler<RateLimiterConfig> {

    RateLimitingHandler() {
        super(custom().build());
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        // TODO Implement rate limiting
        return null;
    }

    @Override
    public int getOrder() {
        return RATE_LIMITING_HANDLER_ORDER;
    }
}
