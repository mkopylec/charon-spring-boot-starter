package com.github.mkopylec.charon.configuration;

public class AsynchronousForwardingConfiguration {

    private ThreadPoolConfiguration threadPoolConfiguration;

    AsynchronousForwardingConfiguration() {
        threadPoolConfiguration = new ThreadPoolConfiguration();
    }

    public ThreadPoolConfiguration getThreadPoolConfiguration() {
        return threadPoolConfiguration;
    }

    void setThreadPoolConfiguration(ThreadPoolConfiguration threadPoolConfiguration) {
        this.threadPoolConfiguration = threadPoolConfiguration;
    }
}
