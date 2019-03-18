package com.github.mkopylec.charon.core;

import com.github.mkopylec.charon.configuration.ResponseCookieRewriterConfigurer;

public class RootPathResponseCookieRewriterConfigurer extends ResponseCookieRewriterConfigurer<RootPathResponseCookieRewriter> {

    protected RootPathResponseCookieRewriterConfigurer() {
        super(new RootPathResponseCookieRewriter());
    }

    public static RootPathResponseCookieRewriterConfigurer rootPathResponseCookieRewriter() {
        return new RootPathResponseCookieRewriterConfigurer();
    }
}
