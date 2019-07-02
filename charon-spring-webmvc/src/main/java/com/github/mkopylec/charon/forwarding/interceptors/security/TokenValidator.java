package com.github.mkopylec.charon.forwarding.interceptors.security;

public interface TokenValidator extends CredentialsValidator {

    @Override
    boolean validate(String token);
}
