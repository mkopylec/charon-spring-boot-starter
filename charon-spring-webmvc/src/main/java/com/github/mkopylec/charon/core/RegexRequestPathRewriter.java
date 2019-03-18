package com.github.mkopylec.charon.core;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class RegexRequestPathRewriter implements RequestPathRewriter {

    protected Pattern incomingRequestPathRegex;
    protected String outgoingRequestPathTemplate;

    protected RegexRequestPathRewriter() {
        incomingRequestPathRegex = compile("/(?<path>.*)");
        outgoingRequestPathTemplate = "/<path>";
    }

    @Override
    public String rewrite(String incomingRequestPath) {
        // TODO Implement
        return null;
    }

    protected void setPaths(String incomingRequestPathRegex, String outgoingRequestPathTemplate) {
        // TODO validate
        this.incomingRequestPathRegex = compile(incomingRequestPathRegex);
        this.outgoingRequestPathTemplate = outgoingRequestPathTemplate;
    }
}
