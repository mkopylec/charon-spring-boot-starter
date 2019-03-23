package com.github.mkopylec.charon.configuration;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class ThreadPoolConfigurer {

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private ThreadPoolConfigurer() {
        threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    }

    public static ThreadPoolConfigurer threadPool() {
        return new ThreadPoolConfigurer();
    }

    public ThreadPoolConfigurer queueCapacity(int queueCapacity) {
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        return this;
    }

    public ThreadPoolConfigurer initialSize(int initialSize) {
        threadPoolTaskExecutor.setCorePoolSize(initialSize);
        return this;
    }

    public ThreadPoolConfigurer maximumSize(int maximumSize) {
        threadPoolTaskExecutor.setMaxPoolSize(maximumSize);
        return this;
    }

    ThreadPoolTaskExecutor getThreadPool() {
        return threadPoolTaskExecutor;
    }
}
