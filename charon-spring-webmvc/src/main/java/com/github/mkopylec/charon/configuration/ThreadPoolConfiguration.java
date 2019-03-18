package com.github.mkopylec.charon.configuration;

import static org.springframework.util.Assert.isTrue;

public class ThreadPoolConfiguration {

    private int queueCapacity;
    private int initialSize;
    private int maximumSize;

    ThreadPoolConfiguration() {
        queueCapacity = 20;
        initialSize = 3;
        maximumSize = 10;
    }

    void validate() {
        isTrue(queueCapacity >= 0, "Invalid thread pool queue capacity: " + queueCapacity);
        isTrue(initialSize >= 0, "Invalid thread pool initial size: " + initialSize);
        isTrue(maximumSize > 0, "Invalid thread pool maximum size: " + maximumSize);
        isTrue(initialSize <= maximumSize, "Thread pool initial size: " + initialSize + " is greater than maximum size: " + maximumSize);
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
