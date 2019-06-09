package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.Configurer;

public class CustomConfigurer extends Configurer<CustomConfiguration> {

    private CustomConfigurer() {
        super(new CustomConfiguration());
    }

    public static CustomConfigurer custom() {
        return new CustomConfigurer();
    }

    public CustomConfigurer set(String name, Object value) {
        configuredObject.setProperty(name, value);
        return this;
    }
}
