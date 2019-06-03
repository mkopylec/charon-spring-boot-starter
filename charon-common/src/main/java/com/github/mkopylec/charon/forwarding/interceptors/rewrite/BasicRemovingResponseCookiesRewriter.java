package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import java.util.List;

import com.github.mkopylec.charon.configuration.Valid;
import org.slf4j.Logger;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;

import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RESPONSE_COOKIE_REWRITER;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

abstract class BasicRemovingResponseCookiesRewriter implements Ordered, Valid {

    private Logger log;

    BasicRemovingResponseCookiesRewriter(Logger log) {
        this.log = log;
    }

    @Override
    public int getOrder() {
        return RESPONSE_COOKIE_REWRITER.getOrder();
    }

    void removeCookies(HttpHeaders headers, String cookieHeaderName) {
        List<String> removedCookies = headers.remove(cookieHeaderName);
        if (isNotEmpty(removedCookies)) {
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
