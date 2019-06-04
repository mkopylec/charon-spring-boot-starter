package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

class CustomResponseRewriterConfigurer extends RequestForwardingInterceptorConfigurer<CustomResponseRewriter> {

    private CustomResponseRewriterConfigurer() {
        super(new CustomResponseRewriter());
    }

    static CustomResponseRewriterConfigurer customResponseRewriter() {
        return new CustomResponseRewriterConfigurer();
    }
}
