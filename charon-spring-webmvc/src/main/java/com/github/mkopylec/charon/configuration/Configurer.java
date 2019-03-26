package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.utils.Valid;

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
