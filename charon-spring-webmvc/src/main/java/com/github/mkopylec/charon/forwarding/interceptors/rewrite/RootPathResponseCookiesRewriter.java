package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import java.net.HttpCookie;
import java.util.List;
import java.util.regex.Pattern;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import org.springframework.http.HttpHeaders;

import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RESPONSE_COOKIE_REWRITER;
import static java.net.HttpCookie.parse;
import static java.util.Collections.emptyList;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpHeaders.SET_COOKIE2;

class RootPathResponseCookiesRewriter implements RequestForwardingInterceptor {

    private static final Pattern cookiePathPattern = compile("Path=/");

    private static final Logger log = getLogger(RootPathResponseCookiesRewriter.class);

    RootPathResponseCookiesRewriter() {
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
        return RESPONSE_COOKIE_REWRITER.getOrder();
    }

    private void rewriteCookies(HttpResponse response, String cookieHeaderName) {
        HttpHeaders responseHeaders = response.getHeaders();
        List<String> responseCookies = responseHeaders.getOrDefault(cookieHeaderName, emptyList());
        List<String> rewrittenResponseCookies = responseCookies.stream()
                .map(this::replaceCookiePath)
                .collect(toList());
        if (isNotEmpty(rewrittenResponseCookies)) {
            log.debug("Response cookies rewritten from {} to {}", responseCookies, rewrittenResponseCookies);
            responseHeaders.put(cookieHeaderName, rewrittenResponseCookies);
        }
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
