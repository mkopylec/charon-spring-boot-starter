package com.github.mkopylec.charon.configuration;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

public class HystrixProperties {

    public static final String HYSTRIX_GROUP_KEY = "charon";
    public static final String HYSTRIX_THREAD_POOL_KEY = "charon-pool";

    /**
     * Flag for enabling and disabling Hystrix circuit breaker.
     */
    private boolean enabled = false;
    /**
     * Properties responsible for hystrix thread pool.
     */
    @NestedConfigurationProperty
    private ThreadPoolProperties threadPool = new ThreadPoolProperties();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ThreadPoolProperties getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPoolProperties threadPool) {
        this.threadPool = threadPool;
    }

    public static class ThreadPoolProperties {

        /**
         * Maximum queue capacity, when it is reached a rejection will occur.
         */
        private int maximumQueueSize = 50;
        /**
         * Number of threads in the pool.
         */
        private int coreSize = 10;

        public int getMaximumQueueSize() {
            return maximumQueueSize;
        }

        public void setMaximumQueueSize(int maximumQueueSize) {
            this.maximumQueueSize = maximumQueueSize;
        }

        public int getCoreSize() {
            return coreSize;
        }

        public void setCoreSize(int coreSize) {
            this.coreSize = coreSize;
        }
    }
}
