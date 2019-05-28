package com.github.mkopylec.charon.forwarding.interceptors;

import org.springframework.core.Ordered;

public enum RequestForwardingInterceptorType implements Ordered {

    FORWARDING_LOGGER(0),
    ASYNCHRONOUS_FORWARDING_HANDLER(100),
    REQUEST_HEADERS_REWRITER(200),
    REQUEST_SERVER_NAME_REWRITER(300),
    REQUEST_HOST_HEADER_REWRITER(400),
    REQUEST_PATH_REWRITER(500),
    RESPONSE_HEADERS_REWRITER(600),
    RESPONSE_COOKIE_REWRITER(700),
    CIRCUIT_BREAKER_HANDLER(800),
    RETRYING_HANDLER(900),
    RATE_LIMITING_HANDLER(1000),
    LATENCY_METER(1100);

    private int order;

    RequestForwardingInterceptorType(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
