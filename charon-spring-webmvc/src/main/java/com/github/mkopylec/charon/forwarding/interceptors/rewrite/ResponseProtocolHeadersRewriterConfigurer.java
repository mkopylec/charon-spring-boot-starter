package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

public class ResponseProtocolHeadersRewriterConfigurer extends RequestForwardingInterceptorConfigurer<ResponseProtocolHeadersRewriter> {

    private ResponseProtocolHeadersRewriterConfigurer() {
        super(new ResponseProtocolHeadersRewriter());
    }

    public static ResponseProtocolHeadersRewriterConfigurer responseProtocolHeadersRewriter() {
        return new ResponseProtocolHeadersRewriterConfigurer();
    }
}
