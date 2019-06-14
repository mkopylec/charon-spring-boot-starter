package com.github.mkopylec.charon.forwarding.interceptors;

import org.springframework.core.Ordered;

public enum RequestForwardingInterceptorType implements Ordered {

    FORWARDING_LOGGER(0),
    ASYNCHRONOUS_FORWARDING_HANDLER(100),
    RATE_METER(200),
    REQUEST_PROTOCOL_HEADERS_REWRITER(300),
    REQUEST_PROXY_HEADERS_REWRITER(400),
    REQUEST_SERVER_NAME_REWRITER(500),
    REQUEST_HOST_HEADER_REWRITER(600),
    REQUEST_PATH_REWRITER(700),
    RESPONSE_PROTOCOL_HEADERS_REWRITER(800),
    RESPONSE_COOKIE_REWRITER(900),
    CIRCUIT_BREAKER_HANDLER(1000),
    RETRYING_HANDLER(1100),
    RATE_LIMITING_HANDLER(1200),
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
