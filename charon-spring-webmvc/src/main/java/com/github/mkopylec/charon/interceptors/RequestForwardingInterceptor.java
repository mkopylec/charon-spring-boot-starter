package com.github.mkopylec.charon.interceptors;

import com.github.mkopylec.charon.forwarding.HttpRequest;
import com.github.mkopylec.charon.forwarding.HttpResponse;
import com.github.mkopylec.charon.forwarding.RequestForwarder;
import com.github.mkopylec.charon.forwarding.RequestForwarding;
import com.github.mkopylec.charon.utils.Valid;

import org.springframework.core.Ordered;

public interface RequestForwardingInterceptor extends Ordered, Valid {

    int ASYNCHRONOUS_FORWARDING_HANDLER_ORDER = 100;
    int REQUEST_SERVER_NAME_REWRITER_ORDER = 200;
    int REQUEST_PATH_REWRITER_ORDER = 300;
    int REQUEST_HEADERS_REWRITER_ORDER = 400;
    int RESPONSE_HEADERS_REWRITER_ORDER = 500;
    int RESPONSE_COOKIE_REWRITER_ORDER = 600;
    int RATE_LIMITING_HANDLER_ORDER = 700;
    int CIRCUIT_BREAKER_HANDLER_ORDER = 800;
    int REQUEST_RETRYING_HANDLER_ORDER = 900;

    HttpResponse forward(HttpRequest request, RequestForwarding forwarding, RequestForwarder forwarder);
}
