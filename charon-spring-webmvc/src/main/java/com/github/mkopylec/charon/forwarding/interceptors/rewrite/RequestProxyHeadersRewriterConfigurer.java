package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

public class RequestProxyHeadersRewriterConfigurer extends RequestForwardingInterceptorConfigurer<RequestProxyHeadersRewriter> {

    private RequestProxyHeadersRewriterConfigurer() {
        super(new RequestProxyHeadersRewriter());
    }

    public static RequestProxyHeadersRewriterConfigurer requestProxyHeadersRewriter() {
        return new RequestProxyHeadersRewriterConfigurer();
    }
}
