package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

public class RequestProtocolHeadersRewriterConfigurer extends RequestForwardingInterceptorConfigurer<RequestProtocolHeadersRewriter> {

    private RequestProtocolHeadersRewriterConfigurer() {
        super(new RequestProtocolHeadersRewriter());
    }

    public static RequestProtocolHeadersRewriterConfigurer requestProtocolHeadersRewriter() {
        return new RequestProtocolHeadersRewriterConfigurer();
    }
}
