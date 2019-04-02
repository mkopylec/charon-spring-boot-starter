package com.github.mkopylec.charon.interceptors.rewrite;

import java.io.IOException;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;

class RemovingResponseCookieRewriter implements RequestForwardingInterceptor {

    RemovingResponseCookieRewriter() {
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) throws IOException {
        HttpResponse response = execution.execute(request);
        // TODO Remove response cookies
        return response;
    }

    @Override
    public int getOrder() {
        return RESPONSE_COOKIE_REWRITER_ORDER;
    }
}
