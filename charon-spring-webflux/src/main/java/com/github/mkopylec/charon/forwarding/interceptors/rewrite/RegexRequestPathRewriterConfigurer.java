package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

public class RegexRequestPathRewriterConfigurer extends RequestForwardingInterceptorConfigurer<RegexRequestPathRewriter> {

    private RegexRequestPathRewriterConfigurer() {
        super(new RegexRequestPathRewriter());
    }

    public static RegexRequestPathRewriterConfigurer regexRequestPathRewriter() {
        return new RegexRequestPathRewriterConfigurer();
    }

    public RegexRequestPathRewriterConfigurer paths(String incomingRequestPathRegex, String outgoingRequestPathTemplate) {
        configuredObject.setPaths(incomingRequestPathRegex, outgoingRequestPathTemplate);
        return this;
    }
}
