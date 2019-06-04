package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.configuration.Valid;
import org.slf4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;

import java.net.HttpCookie;
import java.util.List;

import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RESPONSE_COOKIE_REWRITER;
import static java.net.HttpCookie.parse;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;

class BasicRootPathResponseCookiesRewriter implements Ordered, Valid {

    private Logger log;

    BasicRootPathResponseCookiesRewriter(Logger log) {
        this.log = log;
    }

    @Override
    public int getOrder() {
        return RESPONSE_COOKIE_REWRITER.getOrder();
    }

    void rewriteCookies(HttpHeaders headers, String cookieHeaderName) {
        List<String> responseCookies = headers.getOrDefault(cookieHeaderName, emptyList());
        List<String> rewrittenResponseCookies = responseCookies.stream()
                .map(this::replaceCookiePath)
                .collect(toList());
        if (isNotEmpty(rewrittenResponseCookies)) {
            log.debug("Response cookies rewritten from {} to {}", responseCookies, rewrittenResponseCookies);
            headers.put(cookieHeaderName, rewrittenResponseCookies);
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
