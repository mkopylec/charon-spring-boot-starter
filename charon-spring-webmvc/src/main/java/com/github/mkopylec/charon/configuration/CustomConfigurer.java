package com.github.mkopylec.charon.configuration;

public class CustomConfigurer {

    private CustomConfiguration customConfiguration;

    private CustomConfigurer() {
        customConfiguration = new CustomConfiguration();
    }

    public static CustomConfigurer custom() {
        return new CustomConfigurer();
    }

    public CustomConfigurer set(String name, Object value) {
        customConfiguration.setProperty(name, value);
        return this;
    }

    CustomConfiguration getConfiguration() {
        return customConfiguration;
    }
}
