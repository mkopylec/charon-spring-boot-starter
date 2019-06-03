package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import java.net.URI;

import com.github.mkopylec.charon.configuration.Valid;
import org.slf4j.Logger;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;

import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.REQUEST_HOST_HEADER_REWRITER;
import static org.springframework.http.HttpHeaders.HOST;

class BasicRequestHostHeaderRewriter implements Ordered, Valid {

    private Logger log;

    BasicRequestHostHeaderRewriter(Logger log) {
        this.log = log;
    }

    @Override
    public int getOrder() {
        return REQUEST_HOST_HEADER_REWRITER.getOrder();
    }

    void rewriteHeaders(HttpHeaders headers, URI uri) {
        HttpHeaders oldHeaders = copyHeaders(headers);
        headers.set(HOST, uri.getAuthority());
        log.debug("Request headers rewritten from {} to {}", oldHeaders, headers);
    }

    void logStart(String mappingName) {
        log.trace("[Start] Request 'Host' header rewriting for '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Request 'Host' header rewriting for '{}' request mapping", mappingName);
    }
}
