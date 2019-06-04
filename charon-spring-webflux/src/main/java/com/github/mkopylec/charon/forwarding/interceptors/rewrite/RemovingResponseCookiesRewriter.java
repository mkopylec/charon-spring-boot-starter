package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpHeaders;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpHeaders.SET_COOKIE2;

class RemovingResponseCookiesRewriter extends BasicRemovingResponseCookiesRewriter implements RequestForwardingInterceptor {

    private static final Logger log = getLogger(RemovingResponseCookiesRewriter.class);

    RemovingResponseCookiesRewriter() {
        super(log);
    }

    @Override
    public Mono<HttpResponse> forward(HttpRequest request, HttpRequestExecution execution) {
        logStart(execution.getMappingName());
        return execution.execute(request)
                .doOnSuccess(response -> {
                    HttpHeaders headers = response.headers().asHttpHeaders();
                    removeCookies(headers, SET_COOKIE, response::setHeaders);
                    removeCookies(headers, SET_COOKIE2, response::setHeaders);
                    logEnd(execution.getMappingName());
                });
    }
}
