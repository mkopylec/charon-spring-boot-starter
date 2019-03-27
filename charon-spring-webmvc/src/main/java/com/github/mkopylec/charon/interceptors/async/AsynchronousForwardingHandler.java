package com.github.mkopylec.charon.interceptors.async;

import com.github.mkopylec.charon.forwarding.HttpRequest;
import com.github.mkopylec.charon.forwarding.HttpResponse;
import com.github.mkopylec.charon.forwarding.RequestForwarder;
import com.github.mkopylec.charon.forwarding.RequestForwarding;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

class AsynchronousForwardingHandler implements RequestForwardingInterceptor {

    private boolean enabled;
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    AsynchronousForwardingHandler() {
        enabled = true;
        threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    }

    @Override
    public HttpResponse forward(HttpRequest request, RequestForwarding forwarding, RequestForwarder forwarder) {
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

    void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }
}
