package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

public class RequestHostHeaderRewriterConfigurer extends RequestForwardingInterceptorConfigurer<RequestHostHeaderRewriter> {

    private RequestHostHeaderRewriterConfigurer() {
        super(new RequestHostHeaderRewriter());
    }

    public static RequestHostHeaderRewriterConfigurer requestHostHeaderRewriter() {
        return new RequestHostHeaderRewriterConfigurer();
    }
}
