package com.github.mkopylec.charon.core.interceptors;

import com.github.mkopylec.charon.configuration.ForwardingCompleteInterceptorConfigurer;

public class RootPathResponseCookieRewriterConfigurer extends ForwardingCompleteInterceptorConfigurer<RootPathResponseCookieRewriter> {

    protected RootPathResponseCookieRewriterConfigurer() {
        super(new RootPathResponseCookieRewriter());
    }

    public static RootPathResponseCookieRewriterConfigurer rootPathResponseCookieRewriter() {
        return new RootPathResponseCookieRewriterConfigurer();
    }
}
