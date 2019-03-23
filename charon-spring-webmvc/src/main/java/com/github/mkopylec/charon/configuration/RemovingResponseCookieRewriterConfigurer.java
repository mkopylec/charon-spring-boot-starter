package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.core.interceptors.RemovingResponseCookieRewriter;

public class RemovingResponseCookieRewriterConfigurer extends RequestForwardingInterceptorConfigurer<RemovingResponseCookieRewriter> {

    protected RemovingResponseCookieRewriterConfigurer() {
        super(new RemovingResponseCookieRewriter());
    }

    public static RemovingResponseCookieRewriterConfigurer removingResponseCookieRewriter() {
        return new RemovingResponseCookieRewriterConfigurer();
    }
}
