package com.github.mkopylec.charon.interceptors.rewrite;

import java.io.IOException;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;

class CopyRequestPathRewriter implements RequestForwardingInterceptor {

    CopyRequestPathRewriter() {
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) throws IOException {
        return execution.execute(request);
    }

    @Override
    public int getOrder() {
        return REQUEST_PATH_REWRITER_ORDER;
    }
}
