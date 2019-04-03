package com.github.mkopylec.charon.interceptors.async;

import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptorConfigurer;

public class AsynchronousForwardingHandlerConfigurer extends RequestForwardingInterceptorConfigurer<AsynchronousForwardingHandler> {

    private AsynchronousForwardingHandlerConfigurer() {
        super(new AsynchronousForwardingHandler());
    }

    public static AsynchronousForwardingHandlerConfigurer asynchronousForwardingHandler() {
        return new AsynchronousForwardingHandlerConfigurer();
    }

    public AsynchronousForwardingHandlerConfigurer enabled(boolean enabled) {
        configuredObject.setEnabled(enabled);
        return this;
    }

    public AsynchronousForwardingHandlerConfigurer set(ThreadPoolConfigurer threadPoolConfigurer) {
        configuredObject.setThreadPool(threadPoolConfigurer.configure());
        return this;
    }
}
