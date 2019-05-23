package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import org.springframework.http.HttpHeaders;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.HOST;
import static org.springframework.http.HttpHeaders.TE;
import static org.springframework.http.HttpHeaders.readOnlyHttpHeaders;

class AfterServerNameRequestHeadersRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(AfterServerNameRequestHeadersRewriter.class);

    AfterServerNameRequestHeadersRewriter() {
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] After server name request headers rewriting for '{}' request mapping", execution.getMappingName());
        rewriteHeaders(request);
        HttpResponse response = execution.execute(request);
        log.trace("[End] After server name request headers rewriting for '{}' request mapping", execution.getMappingName());
        return response;
    }

    @Override
    public int getOrder() {
        return AFTER_SERVER_NAME_REQUEST_HEADERS_REWRITER_ORDER;
    }

    private void rewriteHeaders(HttpRequest request) {
        HttpHeaders oldHeaders = readOnlyHttpHeaders(request.getHeaders());
        HttpHeaders headers = request.getHeaders();
        headers.set(HOST, request.getURI().getAuthority());
        headers.remove(TE);
        log.debug("Request headers rewritten from {} to {}", oldHeaders, request.getHeaders());
    }
}
