package com.github.mkopylec.charon.forwarding.interceptors.security;

import com.github.mkopylec.charon.configuration.Configurer;

abstract class CredentialsValidatorConfigurer<V extends CredentialsValidator> extends Configurer<V> {

    CredentialsValidatorConfigurer(V configuredObject) {
        super(configuredObject);
    }

    @Override
    protected V configure() {
        return super.configure();
    }
}
