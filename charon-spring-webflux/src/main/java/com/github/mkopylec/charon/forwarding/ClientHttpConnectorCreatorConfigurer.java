package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.Configurer;

public abstract class ClientHttpConnectorCreatorConfigurer<C extends ClientHttpConnectorCreator> extends Configurer<C> {

    protected ClientHttpConnectorCreatorConfigurer(C configuredObject) {
        super(configuredObject);
    }

    @Override
    protected C configure() {
        return super.configure();
    }
}
