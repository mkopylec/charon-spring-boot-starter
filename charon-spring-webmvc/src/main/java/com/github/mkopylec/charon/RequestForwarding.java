package com.github.mkopylec.charon;

import com.github.mkopylec.charon.configuration.CustomConfiguration;
import com.github.mkopylec.charon.configuration.RequestForwardingConfiguration;

public class RequestForwarding {

    private String name;
    private CustomConfiguration customConfiguration;

    RequestForwarding(RequestForwardingConfiguration configuration) {
        name = configuration.getName();
        customConfiguration = configuration.getCustomConfiguration();
    }

    public String getName() {
        return name;
    }

    public <P> P getCustomProperty(String name) {
        return customConfiguration.getProperty(name);
    }
}
