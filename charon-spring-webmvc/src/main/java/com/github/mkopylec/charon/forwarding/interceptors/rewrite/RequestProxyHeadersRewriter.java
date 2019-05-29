package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import java.util.ArrayList;
import java.util.List;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import org.springframework.http.HttpHeaders;

import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.REQUEST_PROXY_HEADERS_REWRITER;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.HeadersUtils.copyHeaders;
import static java.lang.String.valueOf;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.slf4j.LoggerFactory.getLogger;

class RequestProxyHeadersRewriter implements RequestForwardingInterceptor {

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";
    private static final String X_FORWARDED_HOST = "X-Forwarded-Host";
    private static final String X_FORWARDED_PORT = "X-Forwarded-Port";

    private static final Logger log = getLogger(RequestProxyHeadersRewriter.class);

    RequestProxyHeadersRewriter() {
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Request proxy headers rewriting for '{}' request mapping", execution.getMappingName());
        rewriteHeaders(request);
        HttpResponse response = execution.execute(request);
        log.trace("[End] Request proxy headers rewriting for '{}' request mapping", execution.getMappingName());
        return response;
    }

    @Override
    public int getOrder() {
        return REQUEST_PROXY_HEADERS_REWRITER.getOrder();
    }

    private void rewriteHeaders(HttpRequest request) {
        HttpHeaders oldHeaders = copyHeaders(request.getHeaders());
        HttpHeaders headers = request.getHeaders();
        List<String> forwardedFor = headers.get(X_FORWARDED_FOR);
        if (isEmpty(forwardedFor)) {
            forwardedFor = new ArrayList<>(1);
        }
        forwardedFor.add(request.getURI().getAuthority());
        headers.put(X_FORWARDED_FOR, forwardedFor);
        headers.set(X_FORWARDED_PROTO, request.getURI().getScheme());
        headers.set(X_FORWARDED_HOST, request.getURI().getHost());
        headers.set(X_FORWARDED_PORT, resolvePort(request));
        log.debug("Request headers rewritten from {} to {}", oldHeaders, request.getHeaders());
    }

    private String resolvePort(HttpRequest request) {
        int port = request.getURI().getPort();
        if (port < 0) {
            port = request.getURI().getScheme().equals("https") ? 443 : 80;
        }
        return valueOf(port);
    }
}
