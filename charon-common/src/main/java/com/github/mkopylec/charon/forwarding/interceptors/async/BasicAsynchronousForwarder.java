package com.github.mkopylec.charon.forwarding.interceptors.async;

import com.github.mkopylec.charon.configuration.Valid;

import org.springframework.core.Ordered;

import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.ASYNCHRONOUS_FORWARDING_HANDLER;

class BasicAsynchronousForwarder implements Ordered, Valid {

    ThreadPool threadPool;

    BasicAsynchronousForwarder() {
        threadPool = new ThreadPool();
    }

    @Override
    public int getOrder() {
        return ASYNCHRONOUS_FORWARDING_HANDLER.getOrder();
    }

    void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }
}
