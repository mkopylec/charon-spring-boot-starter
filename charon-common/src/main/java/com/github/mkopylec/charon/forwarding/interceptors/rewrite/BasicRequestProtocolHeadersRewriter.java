package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import java.util.function.Consumer;

import com.github.mkopylec.charon.configuration.Valid;
import org.slf4j.Logger;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;

import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.REQUEST_PROTOCOL_HEADERS_REWRITER;
import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.HttpHeaders.TE;

abstract class BasicRequestProtocolHeadersRewriter implements Ordered, Valid {

    private Logger log;

    BasicRequestProtocolHeadersRewriter(Logger log) {
        this.log = log;
    }

    @Override
    public int getOrder() {
        return REQUEST_PROTOCOL_HEADERS_REWRITER.getOrder();
    }

    void rewriteHeaders(HttpHeaders headers, Consumer<HttpHeaders> headersSetter) {
        HttpHeaders rewrittenHeaders = copyHeaders(headers);
        rewrittenHeaders.set(CONNECTION, "close");
        rewrittenHeaders.remove(TE);
        headersSetter.accept(rewrittenHeaders);
        log.debug("Request headers rewritten from {} to {}", headers, rewrittenHeaders);
    }

    void logStart(String mappingName) {
        log.trace("[Start] Request protocol headers rewriting for '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Request protocol headers rewriting for '{}' request mapping", mappingName);
    }
}
