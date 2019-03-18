package com.github.mkopylec.charon.core;

import com.github.mkopylec.charon.configuration.RequestPathRewriterConfigurer;

public class RegexRequestPathRewriterConfigurer extends RequestPathRewriterConfigurer<RegexRequestPathRewriter> {

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
