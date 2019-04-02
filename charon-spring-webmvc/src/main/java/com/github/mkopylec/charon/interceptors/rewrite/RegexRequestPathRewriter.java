package com.github.mkopylec.charon.interceptors.rewrite;

import java.io.IOException;
import java.util.regex.Pattern;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
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
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) throws IOException {
        // TODO Rewrite request path
        return execution.execute(request);
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
