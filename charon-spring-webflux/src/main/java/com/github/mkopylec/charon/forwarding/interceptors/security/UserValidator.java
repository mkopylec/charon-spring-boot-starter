package com.github.mkopylec.charon.forwarding.interceptors.security;

import reactor.core.publisher.Mono;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.Base64Utils.decodeFromString;

public interface UserValidator extends CredentialsValidator {

    @Override
    default Mono<Boolean> validate(String credentials) {
        String[] splitCredentials = new String(decodeFromString(credentials), UTF_8).split(":");
        String password = splitCredentials.length > 1 ? splitCredentials[1] : null;
        return validate(splitCredentials[0], password);
    }

    Mono<Boolean> validate(String userName, String password);
}
