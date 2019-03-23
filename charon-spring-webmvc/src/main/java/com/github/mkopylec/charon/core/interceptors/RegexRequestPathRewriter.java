package com.github.mkopylec.charon.core.interceptors;

import java.util.regex.Pattern;

import com.github.mkopylec.charon.core.HttpRequest;
import com.github.mkopylec.charon.core.HttpResponse;
import com.github.mkopylec.charon.core.RequestForwarder;
import com.github.mkopylec.charon.core.RequestForwarding;

import static java.util.regex.Pattern.compile;

public class RegexRequestPathRewriter implements RequestForwardingInterceptor {

    protected Pattern incomingRequestPathRegex;
    protected String outgoingRequestPathTemplate;

    public RegexRequestPathRewriter() {
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

    public void setPaths(String incomingRequestPathRegex, String outgoingRequestPathTemplate) {
        this.incomingRequestPathRegex = compile(incomingRequestPathRegex);
        this.outgoingRequestPathTemplate = outgoingRequestPathTemplate;
    }
}
