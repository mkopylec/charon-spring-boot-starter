package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.core.interceptors.RootPathResponseCookieRewriter;

public class RootPathResponseCookieRewriterConfigurer extends RequestForwardingInterceptorConfigurer<RootPathResponseCookieRewriter> {

    protected RootPathResponseCookieRewriterConfigurer() {
        super(new RootPathResponseCookieRewriter());
    }

    public static RootPathResponseCookieRewriterConfigurer rootPathResponseCookieRewriter() {
        return new RootPathResponseCookieRewriterConfigurer();
    }
}
