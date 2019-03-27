package com.github.mkopylec.charon.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.HttpRequest;
import com.github.mkopylec.charon.forwarding.HttpResponse;
import com.github.mkopylec.charon.forwarding.RequestForwarder;
import com.github.mkopylec.charon.forwarding.RequestForwarding;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;

class CopyRequestPathRewriter implements RequestForwardingInterceptor {

    CopyRequestPathRewriter() {
    }

    @Override
    public HttpResponse forward(HttpRequest request, RequestForwarding forwarding, RequestForwarder forwarder) {
        return forwarder.forward(request, forwarding);
    }

    @Override
    public int getOrder() {
        return REQUEST_PATH_REWRITER_ORDER;
    }
}
