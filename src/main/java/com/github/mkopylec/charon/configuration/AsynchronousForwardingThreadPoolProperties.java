package com.github.mkopylec.charon.configuration;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

public class AsynchronousForwardingThreadPoolProperties {

    /**
     * Thread pool executor queue capacity.
     */
    private int queueCapacity = 50;
    /**
     * Properties responsible for number of threads in thread pool executor.
     */
    @NestedConfigurationProperty
    private Size size = new Size();

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public static class Size {

        /**
         * Thread pool executor initial number of threads.
         */
        private int initial = 5;
        /**
         * Thread pool executor maximum number of threads.
         */
        private int maximum = 30;

        public int getInitial() {
            return initial;
        }

        public void setInitial(int initial) {
            this.initial = initial;
        }

        public int getMaximum() {
            return maximum;
        }

        public void setMaximum(int maximum) {
            this.maximum = maximum;
        }
    }
}
