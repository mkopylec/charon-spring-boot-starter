package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

class RateLimiter extends BasicRateLimiter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(RateLimiter.class);

    RateLimiter() {
        super(log);
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        io.github.resilience4j.ratelimiter.RateLimiter rateLimiter = registry.rateLimiter(execution.getMappingName());
        setupMetrics(registry -> createMetrics(registry, execution.getMappingName()));
        HttpResponse response = rateLimiter.executeSupplier(() -> execution.execute(request));
        logEnd(execution.getMappingName());
        return response;
    }
}
