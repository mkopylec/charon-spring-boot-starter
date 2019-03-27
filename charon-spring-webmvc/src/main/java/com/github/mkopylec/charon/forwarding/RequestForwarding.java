package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.CustomConfiguration;
import com.github.mkopylec.charon.configuration.TimeoutConfiguration;

public class RequestForwarding {

    private String name;
    private TimeoutConfiguration timeoutConfiguration;
    private CustomConfiguration customConfiguration;

    RequestForwarding(String name, TimeoutConfiguration timeoutConfiguration, CustomConfiguration customConfiguration) {
        this.name = name;
        this.timeoutConfiguration = timeoutConfiguration;
        this.customConfiguration = customConfiguration;
    }

    public String getName() {
        return name;
    }

    public TimeoutConfiguration getTimeoutConfiguration() {
        return timeoutConfiguration;
    }

    public <P> P getCustomProperty(String name) {
        return customConfiguration.getProperty(name);
    }
}
