package com.github.mkopylec.charon.forwarding.interceptors;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

public class RequestForwardingInterceptorType {

    public static final RequestForwardingInterceptorType FORWARDING_LOGGER = new RequestForwardingInterceptorType(0);
    public static final RequestForwardingInterceptorType ASYNCHRONOUS_FORWARDING_HANDLER = new RequestForwardingInterceptorType(100);
    public static final RequestForwardingInterceptorType RATE_METER = new RequestForwardingInterceptorType(200);
    public static final RequestForwardingInterceptorType REQUEST_PROTOCOL_HEADERS_REWRITER = new RequestForwardingInterceptorType(300);
    public static final RequestForwardingInterceptorType REQUEST_PROXY_HEADERS_REWRITER = new RequestForwardingInterceptorType(400);
    public static final RequestForwardingInterceptorType REQUEST_SERVER_NAME_REWRITER = new RequestForwardingInterceptorType(500);
    public static final RequestForwardingInterceptorType REQUEST_HOST_HEADER_REWRITER = new RequestForwardingInterceptorType(600);
    public static final RequestForwardingInterceptorType REQUEST_PATH_REWRITER = new RequestForwardingInterceptorType(700);
    public static final RequestForwardingInterceptorType RESPONSE_PROTOCOL_HEADERS_REWRITER = new RequestForwardingInterceptorType(800);
    public static final RequestForwardingInterceptorType RESPONSE_COOKIE_REWRITER = new RequestForwardingInterceptorType(900);
    public static final RequestForwardingInterceptorType CIRCUIT_BREAKER_HANDLER = new RequestForwardingInterceptorType(1000);
    public static final RequestForwardingInterceptorType RETRYING_HANDLER = new RequestForwardingInterceptorType(1100);
    public static final RequestForwardingInterceptorType RATE_LIMITING_HANDLER = new RequestForwardingInterceptorType(1200);
    public static final RequestForwardingInterceptorType LATENCY_METER = new RequestForwardingInterceptorType(LOWEST_PRECEDENCE);

    private int order;

    public RequestForwardingInterceptorType(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }
}
