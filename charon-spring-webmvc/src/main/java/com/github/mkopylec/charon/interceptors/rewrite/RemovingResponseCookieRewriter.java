package com.github.mkopylec.charon.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.HttpRequest;
import com.github.mkopylec.charon.forwarding.HttpResponse;
import com.github.mkopylec.charon.forwarding.RequestForwarder;
import com.github.mkopylec.charon.forwarding.RequestForwarding;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;

class RemovingResponseCookieRewriter implements RequestForwardingInterceptor {

    RemovingResponseCookieRewriter() {
    }

    @Override
    public HttpResponse forward(HttpRequest request, RequestForwarding forwarding, RequestForwarder forwarder) {
        HttpResponse response = forwarder.forward(request, forwarding);
        // TODO Remove response cookies
        return response;
    }

    @Override
    public int getOrder() {
        return RESPONSE_COOKIE_REWRITER_ORDER;
    }
}
