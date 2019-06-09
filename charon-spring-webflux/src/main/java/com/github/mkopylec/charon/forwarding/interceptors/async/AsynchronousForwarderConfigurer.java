package com.github.mkopylec.charon.forwarding.interceptors.async;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

public class AsynchronousForwarderConfigurer extends RequestForwardingInterceptorConfigurer<AsynchronousForwarder> {

    private AsynchronousForwarderConfigurer() {
        super(new AsynchronousForwarder());
    }

    public static AsynchronousForwarderConfigurer asynchronousForwarder() {
        return new AsynchronousForwarderConfigurer();
    }

    public AsynchronousForwarderConfigurer set(ThreadPoolConfigurer threadPoolConfigurer) {
        configuredObject.setThreadPool(threadPoolConfigurer.configure());
        return this;
    }
}
