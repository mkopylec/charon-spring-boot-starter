package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

public class RootPathResponseCookiesRewriterConfigurer extends RequestForwardingInterceptorConfigurer<RootPathResponseCookiesRewriter> {

    private RootPathResponseCookiesRewriterConfigurer() {
        super(new RootPathResponseCookiesRewriter());
    }

    public static RootPathResponseCookiesRewriterConfigurer rootPathResponseCookiesRewriter() {
        return new RootPathResponseCookiesRewriterConfigurer();
    }
}
