package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

class CustomResponseStatusRewriterConfigurer extends RequestForwardingInterceptorConfigurer<CustomResponseStatusRewriter> {

    private CustomResponseStatusRewriterConfigurer() {
        super(new CustomResponseStatusRewriter());
    }

    static CustomResponseStatusRewriterConfigurer customResponseStatusRewriter() {
        return new CustomResponseStatusRewriterConfigurer();
    }
}
