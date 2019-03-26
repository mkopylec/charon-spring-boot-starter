package com.github.mkopylec.charon.configuration;

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
