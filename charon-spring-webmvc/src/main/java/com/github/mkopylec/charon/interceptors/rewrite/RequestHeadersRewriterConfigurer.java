package com.github.mkopylec.charon.interceptors.rewrite;

import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptorConfigurer;

public class RequestHeadersRewriterConfigurer extends RequestForwardingInterceptorConfigurer<RequestHeadersRewriter> {

    private RequestHeadersRewriterConfigurer() {
        super(new RequestHeadersRewriter());
    }

    public static RequestHeadersRewriterConfigurer requestHeadersRewriter() {
        return new RequestHeadersRewriterConfigurer();
    }
}
