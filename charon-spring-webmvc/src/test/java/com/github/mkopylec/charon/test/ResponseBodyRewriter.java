package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

class ResponseBodyRewriter extends CommonResponseBodyRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(ResponseBodyRewriter.class);

    ResponseBodyRewriter() {
        super(log);
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        HttpResponse response = execution.execute(request);
        rewriteResponse(response);
        logEnd(execution.getMappingName());
        return response;
    }

    private void rewriteResponse(HttpResponse response) {
        String oldBody = new String(response.getBodyAsBytes());
        response.setBody("Rewritten response body".getBytes());
        log.debug("Response body rewritten from '{}' to '{}'", oldBody, new String(response.getBodyAsBytes()));
    }
}
