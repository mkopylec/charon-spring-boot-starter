package com.github.mkopylec.charon.forwarding.interceptors.security;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Base64.getDecoder;

interface BasicUserValidator {

    default User extractUser(String credentials) {
        try {
            byte[] bytes = credentials.isEmpty() ? new byte[0] : getDecoder().decode(credentials);
            String[] splitCredentials = new String(bytes, UTF_8).split(":");
            String password = splitCredentials.length > 1 ? splitCredentials[1] : null;
            return new User(splitCredentials[0], password);
        } catch (Exception e) {
            return new User();
        }
    }
}
