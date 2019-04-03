package com.github.mkopylec.charon.interceptors.async;

import com.github.mkopylec.charon.interceptors.HttpRequest;
import com.github.mkopylec.charon.interceptors.HttpRequestExecution;
import com.github.mkopylec.charon.interceptors.HttpResponse;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;

class AsynchronousForwardingHandler implements RequestForwardingInterceptor {

    private boolean enabled;
    private ThreadPool threadPool;

    AsynchronousForwardingHandler() {
        enabled = true;
        threadPool = new ThreadPool();
    }

    @Override
    public HttpResponse forward(HttpRequest request, HttpRequestExecution execution) {
        // TODO Implement async forwarding
        return null;
    }

    @Override
    public int getOrder() {
        return ASYNCHRONOUS_FORWARDING_HANDLER_ORDER;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }
}
