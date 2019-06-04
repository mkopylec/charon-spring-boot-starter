package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.configuration.Valid;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.REQUEST_PROXY_HEADERS_REWRITER;
import static java.lang.String.valueOf;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

abstract class BasicRequestProxyHeadersRewriter implements Ordered, Valid {

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";
    private static final String X_FORWARDED_HOST = "X-Forwarded-Host";
    private static final String X_FORWARDED_PORT = "X-Forwarded-Port";

    private Logger log;

    BasicRequestProxyHeadersRewriter(Logger log) {
        this.log = log;
    }

    @Override
    public int getOrder() {
        return REQUEST_PROXY_HEADERS_REWRITER.getOrder();
    }

    void rewriteHeaders(HttpHeaders headers, URI uri) {
        HttpHeaders oldHeaders = copyHeaders(headers);
        List<String> forwardedFor = headers.get(X_FORWARDED_FOR);
        if (isEmpty(forwardedFor)) {
            forwardedFor = new ArrayList<>(1);
        }
        forwardedFor.add(uri.getAuthority());
        headers.put(X_FORWARDED_FOR, forwardedFor);
        headers.set(X_FORWARDED_PROTO, uri.getScheme());
        headers.set(X_FORWARDED_HOST, uri.getHost());
        headers.set(X_FORWARDED_PORT, resolvePort(uri));
        log.debug("Request headers rewritten from {} to {}", oldHeaders, headers);
    }

    void logStart(String mappingName) {
        log.trace("[Start] Request proxy headers rewriting for '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Request proxy headers rewriting for '{}' request mapping", mappingName);
    }

    private String resolvePort(URI uri) {
        int port = uri.getPort();
        if (port < 0) {
            port = uri.getScheme().equals("https") ? 443 : 80;
        }
        return valueOf(port);
    }
}
