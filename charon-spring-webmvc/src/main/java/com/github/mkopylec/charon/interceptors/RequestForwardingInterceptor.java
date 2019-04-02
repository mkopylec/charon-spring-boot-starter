package com.github.mkopylec.charon.interceptors;

import java.io.IOException;

import com.github.mkopylec.charon.configuration.Valid;

import org.springframework.core.Ordered;

// TODO WebClient will not support that kind of interceptors? Need to use WebClient and RestTemplate build-in intercepting mechanism?
public interface RequestForwardingInterceptor extends Ordered, Valid {

    int ASYNCHRONOUS_FORWARDING_HANDLER_ORDER = 100;
    int REQUEST_SERVER_NAME_REWRITER_ORDER = 200;
    int REQUEST_PATH_REWRITER_ORDER = 300;
    int REQUEST_HEADERS_REWRITER_ORDER = 400;
    int REQUEST_COOKIE_REWRITER_ORDER = 500;
    int RESPONSE_HEADERS_REWRITER_ORDER = 600;
    int RESPONSE_COOKIE_REWRITER_ORDER = 700;
    int RATE_LIMITING_HANDLER_ORDER = 800;
    int CIRCUIT_BREAKER_HANDLER_ORDER = 900;
    int REQUEST_RETRYING_HANDLER_ORDER = 1000;
    int LATENCY_METER_ORDER = 1100;

    HttpResponse forward(HttpRequest request, HttpRequestExecution execution) throws IOException;
}
