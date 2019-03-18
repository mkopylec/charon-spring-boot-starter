package com.github.mkopylec.charon.core.interceptors;

import java.util.regex.Pattern;

import com.github.mkopylec.charon.core.HttpRequest;

import static java.util.regex.Pattern.compile;

public class RegexRequestPathRewriter implements ForwardingStartInterceptor {

    protected Pattern incomingRequestPathRegex;
    protected String outgoingRequestPathTemplate;

    protected RegexRequestPathRewriter() {
        incomingRequestPathRegex = compile("/(?<path>.*)");
        outgoingRequestPathTemplate = "/<path>";
    }

    @Override
    public void onStart(String traceId, HttpRequest outgoingRequest) {
        // TODO Implement
    }

    protected void setPaths(String incomingRequestPathRegex, String outgoingRequestPathTemplate) {
        // TODO validate
        this.incomingRequestPathRegex = compile(incomingRequestPathRegex);
        this.outgoingRequestPathTemplate = outgoingRequestPathTemplate;
    }
}
