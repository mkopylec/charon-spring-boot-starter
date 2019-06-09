package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.Configurer;

public abstract class ClientHttpRequestFactoryCreatorConfigurer<C extends ClientHttpRequestFactoryCreator> extends Configurer<C> {

    protected ClientHttpRequestFactoryCreatorConfigurer(C configuredObject) {
        super(configuredObject);
    }

    @Override
    protected C configure() {
        return super.configure();
    }
}
