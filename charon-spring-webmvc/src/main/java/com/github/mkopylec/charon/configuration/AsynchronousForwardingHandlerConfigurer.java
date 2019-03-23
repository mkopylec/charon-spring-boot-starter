package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.core.interceptors.AsynchronousForwardingHandler;

public class AsynchronousForwardingHandlerConfigurer extends RequestForwardingInterceptorConfigurer<AsynchronousForwardingHandler> {

    protected AsynchronousForwardingHandlerConfigurer() {
        super(new AsynchronousForwardingHandler());
    }

    public static AsynchronousForwardingHandlerConfigurer asynchronousForwardingHandler() {
        return new AsynchronousForwardingHandlerConfigurer();
    }

    public AsynchronousForwardingHandlerConfigurer enabled(boolean enabled) {
        configure(retryingHandler -> retryingHandler.setEnabled(enabled));
        return this;
    }

    public AsynchronousForwardingHandlerConfigurer set(ThreadPoolConfigurer threadPoolConfigurer) {
        configure(asynchronousForwardingHandler -> asynchronousForwardingHandler.setThreadPoolTaskExecutor(threadPoolConfigurer.getThreadPool()));
        return this;
    }

    @Override
    AsynchronousForwardingHandler getRequestForwardingInterceptor() {
        AsynchronousForwardingHandler asynchronousForwardingHandler = super.getRequestForwardingInterceptor();
        asynchronousForwardingHandler.initializeThreadPool();
        return asynchronousForwardingHandler;
    }
}
