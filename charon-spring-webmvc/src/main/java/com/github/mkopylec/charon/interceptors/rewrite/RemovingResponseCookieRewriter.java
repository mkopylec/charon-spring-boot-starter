package com.github.mkopylec.charon.interceptors.rewrite;

import com.github.mkopylec.charon.HttpRequest;
import com.github.mkopylec.charon.HttpResponse;
import com.github.mkopylec.charon.RequestForwarder;
import com.github.mkopylec.charon.RequestForwarding;
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
