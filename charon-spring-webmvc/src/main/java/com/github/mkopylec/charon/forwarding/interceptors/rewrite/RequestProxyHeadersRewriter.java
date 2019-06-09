package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

class RequestProxyHeadersRewriter extends BasicRequestProxyHeadersRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(RequestProxyHeadersRewriter.class);

    RequestProxyHeadersRewriter() {
        super(log);
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        rewriteHeaders(request.getHeaders(), request.getURI(), request::setHeaders);
        HttpResponse response = execution.execute(request);
        logEnd(execution.getMappingName());
        return response;
    }
}
