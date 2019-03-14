package com.github.mkopylec.charon.configuration;

public class AsynchronousForwardingConfigurer {

    private AsynchronousForwardingConfiguration asynchronousForwardingConfiguration;

    private AsynchronousForwardingConfigurer() {
        asynchronousForwardingConfiguration = new AsynchronousForwardingConfiguration();
    }

    public static AsynchronousForwardingConfigurer asynchronousForwarding() {
        return new AsynchronousForwardingConfigurer();
    }

    public AsynchronousForwardingConfigurer configure(ThreadPoolConfigurer threadPoolConfigurer) {
        asynchronousForwardingConfiguration.setThreadPoolConfiguration(threadPoolConfigurer.getConfiguration());
        return this;
    }

    AsynchronousForwardingConfiguration getConfiguration() {
        return asynchronousForwardingConfiguration;
    }
}
