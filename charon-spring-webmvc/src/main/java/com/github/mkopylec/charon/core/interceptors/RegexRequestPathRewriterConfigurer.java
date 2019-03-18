package com.github.mkopylec.charon.core.interceptors;

import com.github.mkopylec.charon.configuration.ForwardingStartInterceptorConfigurer;

public class RegexRequestPathRewriterConfigurer extends ForwardingStartInterceptorConfigurer<RegexRequestPathRewriter> {

    protected RegexRequestPathRewriterConfigurer() {
        super(new RegexRequestPathRewriter());
    }

    public static RegexRequestPathRewriterConfigurer regexRequestPathRewriter() {
        return new RegexRequestPathRewriterConfigurer();
    }

    public RegexRequestPathRewriterConfigurer paths(String incomingRequestPathRegex, String outgoingRequestPathTemplate) {
        configure(regexRequestPathRewriter -> regexRequestPathRewriter.setPaths(incomingRequestPathRegex, outgoingRequestPathTemplate));
        return this;
    }
}
