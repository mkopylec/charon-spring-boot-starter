package com.github.mkopylec.charon.interceptors.rewrite;

import java.util.regex.Pattern;

import com.github.mkopylec.charon.HttpRequest;
import com.github.mkopylec.charon.HttpResponse;
import com.github.mkopylec.charon.RequestForwarder;
import com.github.mkopylec.charon.RequestForwarding;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;

import static java.util.regex.Pattern.compile;

class RegexRequestPathRewriter implements RequestForwardingInterceptor {

    private Pattern incomingRequestPathRegex;
    private String outgoingRequestPathTemplate;

    RegexRequestPathRewriter() {
        incomingRequestPathRegex = compile("/(?<path>.*)");
        outgoingRequestPathTemplate = "/<path>";
    }

    @Override
    public HttpResponse forward(HttpRequest request, RequestForwarding forwarding, RequestForwarder forwarder) {
        // TODO Rewrite request path
        return forwarder.forward(request, forwarding);
    }

    @Override
    public void validate() {
        // TODO validate
    }

    @Override
    public int getOrder() {
        return REQUEST_PATH_REWRITER_ORDER;
    }

    void setPaths(String incomingRequestPathRegex, String outgoingRequestPathTemplate) {
        this.incomingRequestPathRegex = compile(incomingRequestPathRegex);
        this.outgoingRequestPathTemplate = outgoingRequestPathTemplate;
    }
}
