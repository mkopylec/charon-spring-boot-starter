package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import java.net.URI;
import java.util.function.Consumer;

import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import org.slf4j.Logger;

import org.springframework.http.HttpHeaders;

import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.REQUEST_HOST_HEADER_REWRITER;
import static org.springframework.http.HttpHeaders.HOST;

abstract class CommonRequestHostHeaderRewriter implements Valid {

    private Logger log;

    CommonRequestHostHeaderRewriter(Logger log) {
        this.log = log;
    }

    public RequestForwardingInterceptorType getType() {
        return REQUEST_HOST_HEADER_REWRITER;
    }

    void rewriteHeaders(HttpHeaders headers, URI uri, Consumer<HttpHeaders> headersSetter) {
        HttpHeaders rewrittenHeaders = copyHeaders(headers);
        rewrittenHeaders.set(HOST, uri.getAuthority());
        headersSetter.accept(rewrittenHeaders);
        log.debug("Request headers rewritten from {} to {}", headers, rewrittenHeaders);
    }

    void logStart(String mappingName) {
        log.trace("[Start] Request 'Host' header rewriting for '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Request 'Host' header rewriting for '{}' request mapping", mappingName);
    }
}
