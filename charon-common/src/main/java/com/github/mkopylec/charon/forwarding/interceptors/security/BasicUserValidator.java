package com.github.mkopylec.charon.forwarding.interceptors.security;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.Base64Utils.decodeFromString;

interface BasicUserValidator {

    default User extractUser(String credentials) {
        try {
            String[] splitCredentials = new String(decodeFromString(credentials), UTF_8).split(":");
            String password = splitCredentials.length > 1 ? splitCredentials[1] : null;
            return new User(splitCredentials[0], password);
        } catch (Exception e) {
            return new User();
        }
    }
}
