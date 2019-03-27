package com.github.mkopylec.charon.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.HttpRequest;
import com.github.mkopylec.charon.forwarding.HttpResponse;
import com.github.mkopylec.charon.forwarding.RequestForwarder;
import com.github.mkopylec.charon.forwarding.RequestForwarding;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;

class RequestHeadersRewriter implements RequestForwardingInterceptor {

    RequestHeadersRewriter() {
    }

    @Override
    public HttpResponse forward(HttpRequest request, RequestForwarding forwarding, RequestForwarder forwarder) {
        // TODO Rewrite headers
        return forwarder.forward(request, forwarding);
    }

    @Override
    public int getOrder() {
        return REQUEST_HEADERS_REWRITER_ORDER;
    }
}
