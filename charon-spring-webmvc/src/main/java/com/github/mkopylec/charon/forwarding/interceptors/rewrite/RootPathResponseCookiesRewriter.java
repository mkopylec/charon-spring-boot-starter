package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpHeaders.SET_COOKIE2;

class RootPathResponseCookiesRewriter extends CommonRootPathResponseCookiesRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(RootPathResponseCookiesRewriter.class);

    RootPathResponseCookiesRewriter() {
        super(log);
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        HttpResponse response = execution.execute(request);
        rewriteCookies(response.getHeaders(), SET_COOKIE, response::setHeaders);
        rewriteCookies(response.getHeaders(), SET_COOKIE2, response::setHeaders);
        logEnd(execution.getMappingName());
        return response;
    }
}
