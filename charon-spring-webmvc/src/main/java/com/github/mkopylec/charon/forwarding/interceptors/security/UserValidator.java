package com.github.mkopylec.charon.forwarding.interceptors.security;

public interface UserValidator extends BasicUserValidator, CredentialsValidator {

    @Override
    default boolean validate(String credentials) {
        User user = extractUser(credentials);
        return !user.isEmpty() && validate(user);
    }

    boolean validate(User user);
}
