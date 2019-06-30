package com.github.mkopylec.charon.forwarding.interceptors.security;

public abstract class TokenValidatorConfigurer<V extends TokenValidator> extends CredentialsValidatorConfigurer<V> {

    protected TokenValidatorConfigurer(V configuredObject) {
        super(configuredObject);
    }
}
