package com.github.mkopylec.charon.interceptors;

import com.github.mkopylec.charon.configuration.Valid;

import org.springframework.core.Ordered;

public interface RequestForwardingInterceptor extends Ordered, Valid {

    int LOGGER_ORDER = 0;
    int ASYNCHRONOUS_FORWARDING_HANDLER_ORDER = 100;
    int BEFORE_SERVER_NAME_REQUEST_HEADERS_REWRITER_ORDER = 200;
    int REQUEST_SERVER_NAME_REWRITER_ORDER = 300;
    int AFTER_SERVER_NAME_REQUEST_HEADERS_REWRITER_ORDER = 400;
    int REQUEST_PATH_REWRITER_ORDER = 500;
    int RESPONSE_HEADERS_REWRITER_ORDER = 600;
    int RESPONSE_COOKIE_REWRITER_ORDER = 700;
    int CIRCUIT_BREAKER_HANDLER_ORDER = 800;
    int RETRYING_HANDLER_ORDER = 900;
    int RATE_LIMITING_HANDLER_ORDER = 1000;
    int LATENCY_METER_ORDER = 1100;

    HttpResponse forward(HttpRequest request, HttpRequestExecution execution);
}
