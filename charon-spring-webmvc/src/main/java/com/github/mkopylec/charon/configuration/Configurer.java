package com.github.mkopylec.charon.configuration;

public abstract class Configurer<O extends Valid> {

    protected O configuredObject;

    protected Configurer(O configuredObject) {
        this.configuredObject = configuredObject;
    }

    protected O configure() {
        configuredObject.validate();
        return configuredObject;
    }
}
