package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

public class RootPathResponseCookieRewriterConfigurer extends RequestForwardingInterceptorConfigurer<RootPathResponseCookieRewriter> {

    private RootPathResponseCookieRewriterConfigurer() {
        super(new RootPathResponseCookieRewriter());
    }

    public static RootPathResponseCookieRewriterConfigurer rootPathResponseCookieRewriter() {
        return new RootPathResponseCookieRewriterConfigurer();
    }
}
