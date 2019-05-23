package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

public class ResponseHeadersRewriterConfigurer extends RequestForwardingInterceptorConfigurer<ResponseHeadersRewriter> {

    private ResponseHeadersRewriterConfigurer() {
        super(new ResponseHeadersRewriter());
    }

    public static ResponseHeadersRewriterConfigurer responseHeadersRewriter() {
        return new ResponseHeadersRewriterConfigurer();
    }
}
