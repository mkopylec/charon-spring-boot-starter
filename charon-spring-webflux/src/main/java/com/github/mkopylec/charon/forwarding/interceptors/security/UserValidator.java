package com.github.mkopylec.charon.forwarding.interceptors.security;

import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.just;

public interface UserValidator extends BasicUserValidator, CredentialsValidator {

    @Override
    default Mono<Boolean> validate(String credentials) {
        User user = extractUser(credentials);
        return user.isEmpty() ? just(false) : validate(user);
    }

    Mono<Boolean> validate(User user);
}
