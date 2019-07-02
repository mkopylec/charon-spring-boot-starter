package com.github.mkopylec.charon.forwarding.interceptors.security;

import reactor.core.publisher.Mono;

public interface TokenValidator extends CredentialsValidator {

    @Override
    Mono<Boolean> validate(String token);
}
