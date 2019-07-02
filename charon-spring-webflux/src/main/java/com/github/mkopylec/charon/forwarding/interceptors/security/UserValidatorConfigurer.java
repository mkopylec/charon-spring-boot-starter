package com.github.mkopylec.charon.forwarding.interceptors.security;

public abstract class UserValidatorConfigurer<V extends UserValidator> extends CredentialsValidatorConfigurer<V> {

    protected UserValidatorConfigurer(V configuredObject) {
        super(configuredObject);
    }
}
