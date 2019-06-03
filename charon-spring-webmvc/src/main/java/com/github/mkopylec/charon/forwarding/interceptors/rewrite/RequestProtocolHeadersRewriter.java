package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import org.springframework.http.HttpHeaders;

import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.REQUEST_PROTOCOL_HEADERS_REWRITER;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.HttpHeaders.TE;

class RequestProtocolHeadersRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(RequestProtocolHeadersRewriter.class);

    RequestProtocolHeadersRewriter() {
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Request protocol headers rewriting for '{}' request mapping", execution.getMappingName());
        rewriteHeaders(request);
        HttpResponse response = execution.execute(request);
        log.trace("[End] Request protocol headers rewriting for '{}' request mapping", execution.getMappingName());
        return response;
    }

    @Override
    public int getOrder() {
        return REQUEST_PROTOCOL_HEADERS_REWRITER.getOrder();
    }

    private void rewriteHeaders(HttpRequest request) {
        HttpHeaders oldHeaders = copyHeaders(request.getHeaders());
        HttpHeaders headers = request.getHeaders();
        headers.set(CONNECTION, "close");
        headers.remove(TE);
        log.debug("Request headers rewritten from {} to {}", oldHeaders, request.getHeaders());
    }
}
