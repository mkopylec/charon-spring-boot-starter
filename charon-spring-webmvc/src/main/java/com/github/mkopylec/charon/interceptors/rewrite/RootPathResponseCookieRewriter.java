package com.github.mkopylec.charon.interceptors.rewrite;

import java.net.HttpCookie;
import java.util.List;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import org.springframework.http.HttpHeaders;

import static java.net.HttpCookie.parse;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpHeaders.SET_COOKIE2;

class RootPathResponseCookieRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(RootPathResponseCookieRewriter.class);

    RootPathResponseCookieRewriter() {
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        log.trace("[Start] Root path response cookies rewriting for '{}' request mapping", execution.getMappingName());
        HttpResponse response = execution.execute(request);
        rewriteCookies(response, SET_COOKIE);
        rewriteCookies(response, SET_COOKIE2);
        log.trace("[End] Root path response cookies rewriting for '{}' request mapping", execution.getMappingName());
        return response;
    }

    @Override
    public int getOrder() {
        return RESPONSE_COOKIE_REWRITER_ORDER;
    }

    private void rewriteCookies(HttpResponse response, String cookieHeaderName) {
        HttpHeaders responseHeaders = response.getHeaders();
        List<String> responseCookies = responseHeaders.getOrDefault(cookieHeaderName, emptyList());
        List<String> rewrittenResponseCookies = responseCookies.stream()
                .flatMap(setCookieHeader -> parse(setCookieHeader).stream())
                .peek(cookie -> cookie.setPath("/"))
                .map(HttpCookie::toString)
                .collect(toList());
        if (isNotEmpty(rewrittenResponseCookies)) {
            log.debug("Response cookies rewritten from {} to {}", responseCookies, rewrittenResponseCookies);
            responseHeaders.put(cookieHeaderName, rewrittenResponseCookies);
        }
    }
}
