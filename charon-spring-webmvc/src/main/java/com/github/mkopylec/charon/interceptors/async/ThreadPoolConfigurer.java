package com.github.mkopylec.charon.interceptors.async;

import com.github.mkopylec.charon.configuration.Configurer;

public class ThreadPoolConfigurer extends Configurer<ThreadPool> {

    private ThreadPoolConfigurer() {
        super(new ThreadPool());
    }

    public static ThreadPoolConfigurer threadPool() {
        return new ThreadPoolConfigurer();
    }

    public ThreadPoolConfigurer queueCapacity(int queueCapacity) {
        configuredObject.setQueueCapacity(queueCapacity);
        return this;
    }

    public ThreadPoolConfigurer initialSize(int initialSize) {
        configuredObject.setCorePoolSize(initialSize);
        return this;
    }

    public ThreadPoolConfigurer maximumSize(int maximumSize) {
        configuredObject.setMaxPoolSize(maximumSize);
        return this;
    }

    @Override
    protected ThreadPool configure() {
        ThreadPool threadPool = super.configure();
        threadPool.initialize();
        return threadPool;
    }
}
