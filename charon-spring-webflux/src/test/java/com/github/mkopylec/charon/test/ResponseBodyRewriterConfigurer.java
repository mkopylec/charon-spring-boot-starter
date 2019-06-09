package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

class ResponseBodyRewriterConfigurer extends RequestForwardingInterceptorConfigurer<ResponseBodyRewriter> {

    private ResponseBodyRewriterConfigurer() {
        super(new ResponseBodyRewriter());
    }

    static ResponseBodyRewriterConfigurer responseBodyRewriter() {
        return new ResponseBodyRewriterConfigurer();
    }
}
