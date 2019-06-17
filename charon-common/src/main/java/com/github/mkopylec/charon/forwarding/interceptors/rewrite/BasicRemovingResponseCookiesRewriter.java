package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import java.util.List;
import java.util.function.Consumer;

import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import org.slf4j.Logger;

import org.springframework.http.HttpHeaders;

import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RESPONSE_COOKIE_REWRITER;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

abstract class BasicRemovingResponseCookiesRewriter implements Valid {

    private Logger log;

    BasicRemovingResponseCookiesRewriter(Logger log) {
        this.log = log;
    }

    public RequestForwardingInterceptorType getType() {
        return RESPONSE_COOKIE_REWRITER;
    }

    void removeCookies(HttpHeaders headers, String cookieHeaderName, Consumer<HttpHeaders> headersSetter) {
        HttpHeaders rewrittenHeaders = copyHeaders(headers);
        List<String> removedCookies = rewrittenHeaders.remove(cookieHeaderName);
        if (isNotEmpty(removedCookies)) {
            headersSetter.accept(rewrittenHeaders);
            log.debug("Cookies {} removed from response", removedCookies);
        }
    }

    void logStart(String mappingName) {
        log.trace("[Start] Removing response cookies for '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Removing response cookies for '{}' request mapping", mappingName);
    }
}
