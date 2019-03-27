package com.github.mkopylec.charon.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.HttpRequest;
import com.github.mkopylec.charon.forwarding.HttpResponse;
import com.github.mkopylec.charon.forwarding.RequestForwarder;
import com.github.mkopylec.charon.forwarding.RequestForwarding;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;

class ResponseHeadersRewriter implements RequestForwardingInterceptor {

    ResponseHeadersRewriter() {
    }

    @Override
    public HttpResponse forward(HttpRequest request, RequestForwarding forwarding, RequestForwarder forwarder) {
        HttpResponse response = forwarder.forward(request, forwarding);
        // TODO Rewrite headers
        return response;
    }

    @Override
    public int getOrder() {
        return RESPONSE_HEADERS_REWRITER_ORDER;
    }
}
