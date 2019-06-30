package com.github.mkopylec.charon.forwarding.interceptors.security;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

public class BearerAuthenticatorConfigurer extends RequestForwardingInterceptorConfigurer<BearerAuthenticator> {

    private BearerAuthenticatorConfigurer() {
        super(new BearerAuthenticator());
    }

    public static BearerAuthenticatorConfigurer bearerAuthenticator() {
        return new BearerAuthenticatorConfigurer();
    }

    public BearerAuthenticatorConfigurer tokenValidator(TokenValidatorConfigurer<?> tokenValidatorConfigurer) {
        configuredObject.setCredentialsValidator(tokenValidatorConfigurer.configure());
        return this;
    }

    public BearerAuthenticatorConfigurer realm(String realm) {
        configuredObject.setRealm(realm);
        return this;
    }
}
