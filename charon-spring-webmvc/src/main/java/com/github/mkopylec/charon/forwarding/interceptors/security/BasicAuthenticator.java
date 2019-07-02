package com.github.mkopylec.charon.forwarding.interceptors.security;

import org.slf4j.Logger;

import static com.github.mkopylec.charon.forwarding.interceptors.security.AuthenticationType.BASIC;
import static org.slf4j.LoggerFactory.getLogger;

class BasicAuthenticator extends Authenticator<UserValidator> {

    private static final Logger log = getLogger(BasicAuthenticator.class);

    BasicAuthenticator() {
        super(log, BASIC);
    }
}
