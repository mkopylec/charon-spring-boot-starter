package com.github.mkopylec.charon.configuration;

public class ThreadPoolConfigurer {

    private ThreadPoolConfiguration threadPoolConfiguration;

    private ThreadPoolConfigurer() {
        threadPoolConfiguration = new ThreadPoolConfiguration();
    }

    public static ThreadPoolConfigurer threadPool() {
        return new ThreadPoolConfigurer();
    }

    public ThreadPoolConfigurer queueCapacity(int queueCapacity) {
        threadPoolConfiguration.setQueueCapacity(queueCapacity);
        return this;
    }

    public ThreadPoolConfigurer initialSize(int initialSize) {
        threadPoolConfiguration.setInitialSize(initialSize);
        return this;
    }

    public ThreadPoolConfigurer maximumSize(int maximumSize) {
        threadPoolConfiguration.setMaximumSize(maximumSize);
        return this;
    }

    ThreadPoolConfiguration getConfiguration() {
        return threadPoolConfiguration;
    }
}
