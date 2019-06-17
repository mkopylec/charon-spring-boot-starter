package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import java.net.HttpCookie;
import java.util.List;
import java.util.function.Consumer;

import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;
import org.slf4j.Logger;

import org.springframework.http.HttpHeaders;

import static com.github.mkopylec.charon.forwarding.Utils.copyHeaders;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RESPONSE_COOKIE_REWRITER;
import static java.net.HttpCookie.parse;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;

class BasicRootPathResponseCookiesRewriter implements Valid {

    private Logger log;

    BasicRootPathResponseCookiesRewriter(Logger log) {
        this.log = log;
    }

    public RequestForwardingInterceptorType getType() {
        return RESPONSE_COOKIE_REWRITER;
    }

    void rewriteCookies(HttpHeaders headers, String cookieHeaderName, Consumer<HttpHeaders> headersSetter) {
        HttpHeaders rewrittenHeaders = copyHeaders(headers);
        List<String> responseCookies = rewrittenHeaders.getOrDefault(cookieHeaderName, emptyList());
        List<String> rewrittenResponseCookies = responseCookies.stream()
                .map(this::replaceCookiePath)
                .collect(toList());
        if (isNotEmpty(rewrittenResponseCookies)) {
            rewrittenHeaders.put(cookieHeaderName, rewrittenResponseCookies);
            headersSetter.accept(rewrittenHeaders);
            log.debug("Response cookies rewritten from {} to {}", responseCookies, rewrittenResponseCookies);
        }
    }

    void logStart(String mappingName) {
        log.trace("[Start] Root path response cookies rewriting for '{}' request mapping", mappingName);
    }

    void logEnd(String mappingName) {
        log.trace("[End] Root path response cookies rewriting for '{}' request mapping", mappingName);
    }

    private String replaceCookiePath(String setCookieHeader) {
        if (isBlank(setCookieHeader)) {
            return setCookieHeader;
        }
        List<HttpCookie> cookies = parse(setCookieHeader);
        if (isEmpty(cookies)) {
            return setCookieHeader;
        }
        String path = cookies.get(0).getPath();
        if (path == null) {
            return setCookieHeader + "; Path=/";
        }
        return setCookieHeader.replace("Path=" + path, "Path=/");
    }
}
