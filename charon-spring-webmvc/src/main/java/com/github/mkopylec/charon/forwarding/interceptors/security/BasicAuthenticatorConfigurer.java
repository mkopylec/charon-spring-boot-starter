package com.github.mkopylec.charon.forwarding.interceptors.security;

import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorConfigurer;

public class BasicAuthenticatorConfigurer extends RequestForwardingInterceptorConfigurer<BasicAuthenticator> {

    private BasicAuthenticatorConfigurer() {
        super(new BasicAuthenticator());
    }

    public static BasicAuthenticatorConfigurer basicAuthenticator() {
        return new BasicAuthenticatorConfigurer();
    }

    public BasicAuthenticatorConfigurer userValidator(UserValidatorConfigurer<?> userValidatorConfigurer) {
        configuredObject.setCredentialsValidator(userValidatorConfigurer.configure());
        return this;
    }

    public BasicAuthenticatorConfigurer realm(String realm) {
        configuredObject.setRealm(realm);
        return this;
    }
}
