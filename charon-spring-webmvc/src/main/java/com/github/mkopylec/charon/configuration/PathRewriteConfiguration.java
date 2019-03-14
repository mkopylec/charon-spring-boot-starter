package com.github.mkopylec.charon.configuration;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

// TODO Use https://www.stringtemplate.org/ or similar
public class PathRewriteConfiguration {

    private Pattern incomingPathRegex;
    private String outgoingPathTemplate;

    PathRewriteConfiguration() {
        incomingPathRegex = compile("/(?<path>.*)");
        outgoingPathTemplate = "/<path>";
    }

    public Pattern getIncomingPathRegex() {
        return incomingPathRegex;
    }

    void setIncomingPathRegex(String incomingPathRegex) {
        this.incomingPathRegex = compile(incomingPathRegex);
    }

    public String getOutgoingPathTemplate() {
        return outgoingPathTemplate;
    }

    void setOutgoingPathTemplate(String outgoingPathTemplate) {
        this.outgoingPathTemplate = outgoingPathTemplate;
    }
}
