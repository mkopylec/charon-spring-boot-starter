package com.github.mkopylec.charon.interceptors.rewrite;

import com.github.mkopylec.charon.HttpRequest;
import com.github.mkopylec.charon.HttpResponse;
import com.github.mkopylec.charon.RequestForwarder;
import com.github.mkopylec.charon.RequestForwarding;
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
