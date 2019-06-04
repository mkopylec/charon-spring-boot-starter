package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

class ResponseProtocolHeadersRewriter extends BasicResponseProtocolHeadersRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(ResponseProtocolHeadersRewriter.class);

    ResponseProtocolHeadersRewriter() {
        super(log);
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        HttpResponse response = execution.execute(request);
        rewriteHeaders(response.getHeaders(), response::setHeaders);
        logEnd(execution.getMappingName());
        return response;
    }
}
