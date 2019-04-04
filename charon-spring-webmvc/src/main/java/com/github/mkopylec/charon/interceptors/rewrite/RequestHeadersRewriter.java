package com.github.mkopylec.charon.interceptors.rewrite;

import java.util.ArrayList;
import java.util.List;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import org.springframework.http.HttpHeaders;

import static java.lang.String.valueOf;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.HOST;
import static org.springframework.http.HttpHeaders.TE;

class RequestHeadersRewriter implements RequestForwardingInterceptor {

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";
    private static final String X_FORWARDED_HOST = "X-Forwarded-Host";
    private static final String X_FORWARDED_PORT = "X-Forwarded-Port";

    private static final Logger log = getLogger(RequestHeadersRewriter.class);

    RequestHeadersRewriter() {
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Request headers rewriting for '{}' forwarding", execution.getForwardingName());
        rewriteHeaders(request);
        HttpResponse response = execution.execute(request);
        log.trace("[End] Request headers rewriting for '{}' forwarding", execution.getForwardingName());
        return response;
    }

    @Override
    public int getOrder() {
        return REQUEST_HEADERS_REWRITER_ORDER;
    }

    private void rewriteHeaders(HttpRequest request) {
        rewriteProtocolHeaders(request);
        rewriteForwardedHeaders(request);
        log.debug("Request headers rewritten from {} to {}", request.getOriginalHeaders(), request.getHeaders());
    }

    private void rewriteProtocolHeaders(HttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        headers.set(HOST, request.getURI().getAuthority());
        headers.remove(TE);
    }

    private void rewriteForwardedHeaders(HttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        List<String> forwardedFor = headers.get(X_FORWARDED_FOR);
        if (isEmpty(forwardedFor)) {
            forwardedFor = new ArrayList<>(1);
        }
        forwardedFor.add(request.getOriginalUri().getAuthority());
        headers.put(X_FORWARDED_FOR, forwardedFor);
        headers.set(X_FORWARDED_PROTO, request.getOriginalUri().getScheme());
        headers.set(X_FORWARDED_HOST, request.getOriginalUri().getHost());
        headers.set(X_FORWARDED_PORT, valueOf(request.getOriginalUri().getPort()));
    }
}
