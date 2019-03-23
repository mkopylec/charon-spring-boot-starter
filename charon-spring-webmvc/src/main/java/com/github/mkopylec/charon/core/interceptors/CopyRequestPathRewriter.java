package com.github.mkopylec.charon.core.interceptors;

import com.github.mkopylec.charon.core.HttpRequest;
import com.github.mkopylec.charon.core.HttpResponse;
import com.github.mkopylec.charon.core.RequestForwarder;
import com.github.mkopylec.charon.core.RequestForwarding;

public class CopyRequestPathRewriter implements RequestForwardingInterceptor {

    @Override
    public HttpResponse forward(HttpRequest request, RequestForwarding forwarding, RequestForwarder forwarder) {
        return forwarder.forward(request, forwarding);
    }

    @Override
    public int getOrder() {
        return REQUEST_PATH_REWRITER_ORDER;
    }
}
