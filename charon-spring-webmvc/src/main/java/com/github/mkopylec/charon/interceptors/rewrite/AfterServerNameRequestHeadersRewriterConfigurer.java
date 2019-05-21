package com.github.mkopylec.charon.interceptors.rewrite;

import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptorConfigurer;

public class AfterServerNameRequestHeadersRewriterConfigurer extends RequestForwardingInterceptorConfigurer<AfterServerNameRequestHeadersRewriter> {

    private AfterServerNameRequestHeadersRewriterConfigurer() {
        super(new AfterServerNameRequestHeadersRewriter());
    }

    public static AfterServerNameRequestHeadersRewriterConfigurer afterServerNameRequestHeadersRewriter() {
        return new AfterServerNameRequestHeadersRewriterConfigurer();
    }
}
