package com.github.mkopylec.charon.configuration;

public class ThreadPoolConfiguration {

    private int queueCapacity;
    private int initialSize;
    private int maximumSize;

    ThreadPoolConfiguration() {
        queueCapacity = 20;
        initialSize = 3;
        maximumSize = 10;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public int getInitialSize() {
        return initialSize;
    }

    void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }
}
