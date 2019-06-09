package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

class RegexRequestPathRewriter extends BasicRegexRequestPathRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(RegexRequestPathRewriter.class);

    RegexRequestPathRewriter() {
        super(log);
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        rewritePath(request.getURI(), request::setUri);
        HttpResponse response = execution.execute(request);
        logEnd(execution.getMappingName());
        return response;
    }
}
