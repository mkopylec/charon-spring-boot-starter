package com.github.mkopylec.charon.forwarding.interceptors.async;

import com.github.mkopylec.charon.configuration.Configurer;

public class ThreadPoolConfigurer extends Configurer<ThreadPool> {

    private ThreadPoolConfigurer() {
        super(new ThreadPool());
    }

    public static ThreadPoolConfigurer threadPool() {
        return new ThreadPoolConfigurer();
    }

    public ThreadPoolConfigurer coreSize(int coreSize) {
        configuredObject.setCorePoolSize(coreSize);
        return this;
    }

    public ThreadPoolConfigurer maximumSize(int maximumSize) {
        configuredObject.setMaximumPoolSize(maximumSize);
        return this;
    }

    @Override
    protected ThreadPool configure() {
        return super.configure();
    }
}
