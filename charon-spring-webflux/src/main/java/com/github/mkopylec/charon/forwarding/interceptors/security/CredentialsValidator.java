package com.github.mkopylec.charon.forwarding.interceptors.security;

import com.github.mkopylec.charon.configuration.Valid;
import reactor.core.publisher.Mono;

interface CredentialsValidator extends Valid {

    Mono<Boolean> validate(String credentials);
}
