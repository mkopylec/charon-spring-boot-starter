package com.github.mkopylec.charon.forwarding.interceptors;

import org.springframework.core.Ordered;

public enum RequestForwardingInterceptorType implements Ordered {

    FORWARDING_LOGGER(0),
    ASYNCHRONOUS_FORWARDING_HANDLER(100),
    REQUEST_PROTOCOL_HEADERS_REWRITER(200),
    REQUEST_PROXY_HEADERS_REWRITER(300),
    REQUEST_SERVER_NAME_REWRITER(400),
    REQUEST_HOST_HEADER_REWRITER(500),
    REQUEST_PATH_REWRITER(600),
    RESPONSE_PROTOCOL_HEADERS_REWRITER(700),
    RESPONSE_COOKIE_REWRITER(800),
    CIRCUIT_BREAKER_HANDLER(900),
    RETRYING_HANDLER(1000),
    RATE_LIMITING_HANDLER(1100),
    LATENCY_METER(LOWEST_PRECEDENCE);

    private int order;

    RequestForwardingInterceptorType(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
