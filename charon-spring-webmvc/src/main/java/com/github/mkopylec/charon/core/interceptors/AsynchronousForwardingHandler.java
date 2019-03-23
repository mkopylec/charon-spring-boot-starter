package com.github.mkopylec.charon.core.interceptors;

import com.github.mkopylec.charon.core.HttpRequest;
import com.github.mkopylec.charon.core.HttpResponse;
import com.github.mkopylec.charon.core.RequestForwarder;
import com.github.mkopylec.charon.core.RequestForwarding;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static org.springframework.util.Assert.isTrue;

public class AsynchronousForwardingHandler implements RequestForwardingInterceptor {

    protected boolean enabled;
    protected ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public AsynchronousForwardingHandler() {
        enabled = true;
        threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    }

    @Override
    public HttpResponse forward(HttpRequest request, RequestForwarding forwarding, RequestForwarder forwarder) {
        // TODO Implement async forwarding
        return null;
    }

    @Override
    public void validate() {
        int initialPoolSize = threadPoolTaskExecutor.getCorePoolSize();
        int maximumPoolSize = threadPoolTaskExecutor.getMaxPoolSize();
        isTrue(initialPoolSize >= 0, "Invalid thread pool initial size: " + initialPoolSize);
        isTrue(maximumPoolSize > 0, "Invalid thread pool maximum size: " + maximumPoolSize);
        isTrue(initialPoolSize <= maximumPoolSize, "Thread pool initial size: " + initialPoolSize + " is greater than maximum size: " + maximumPoolSize);
    }

    @Override
    public int getOrder() {
        return ASYNCHRONOUS_FORWARDING_HANDLER_ORDER;
    }

    public void initializeThreadPool() {
        threadPoolTaskExecutor.initialize();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }
}
