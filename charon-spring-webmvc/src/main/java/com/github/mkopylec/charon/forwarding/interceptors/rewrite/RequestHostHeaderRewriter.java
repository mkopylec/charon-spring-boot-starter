package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import org.springframework.http.HttpHeaders;

import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.REQUEST_HOST_HEADER_REWRITER;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.HeadersUtils.copyHeaders;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.HOST;

class RequestHostHeaderRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(RequestHostHeaderRewriter.class);

    RequestHostHeaderRewriter() {
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Request 'Host' header rewriting for '{}' request mapping", execution.getMappingName());
        rewriteHeaders(request);
        HttpResponse response = execution.execute(request);
        log.trace("[End] Request 'Host' header rewriting for '{}' request mapping", execution.getMappingName());
        return response;
    }

    @Override
    public int getOrder() {
        return REQUEST_HOST_HEADER_REWRITER.getOrder();
    }

    private void rewriteHeaders(HttpRequest request) {
        HttpHeaders oldHeaders = copyHeaders(request.getHeaders());
        HttpHeaders headers = request.getHeaders();
        headers.set(HOST, request.getURI().getAuthority());
        log.debug("Request headers rewritten from {} to {}", oldHeaders, request.getHeaders());
    }
}
