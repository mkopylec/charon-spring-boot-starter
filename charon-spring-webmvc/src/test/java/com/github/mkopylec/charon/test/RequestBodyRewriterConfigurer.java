package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

class RequestBodyRewriterConfigurer extends RequestForwardingInterceptorConfigurer<RequestBodyRewriter> {

    private RequestBodyRewriterConfigurer() {
        super(new RequestBodyRewriter());
    }

    static RequestBodyRewriterConfigurer requestBodyRewriter() {
        return new RequestBodyRewriterConfigurer();
    }
}
