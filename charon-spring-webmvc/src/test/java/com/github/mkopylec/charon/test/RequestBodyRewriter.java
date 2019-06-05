package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

class RequestBodyRewriter extends BasicRequestBodyRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(RequestBodyRewriter.class);

    RequestBodyRewriter() {
        super(log);
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        rewriteRequest(request);
        HttpResponse response = execution.execute(request);
        logEnd(execution.getMappingName());
        return response;
    }

    private void rewriteRequest(HttpRequest request) {
        String oldBody = new String(request.getBody());
        request.setBody("Rewritten response body".getBytes());
        log.debug("Request body rewritten from '{}' to '{}'", oldBody, new String(request.getBody()));
    }
}
