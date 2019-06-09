package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

public class RemovingResponseCookiesRewriterConfigurer extends RequestForwardingInterceptorConfigurer<RemovingResponseCookiesRewriter> {

    private RemovingResponseCookiesRewriterConfigurer() {
        super(new RemovingResponseCookiesRewriter());
    }

    public static RemovingResponseCookiesRewriterConfigurer removingResponseCookiesRewriter() {
        return new RemovingResponseCookiesRewriterConfigurer();
    }
}
