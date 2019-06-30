package com.github.mkopylec.charon.forwarding.interceptors.security;

import org.slf4j.Logger;

import static com.github.mkopylec.charon.forwarding.interceptors.security.AuthenticationType.BEARER;
import static org.slf4j.LoggerFactory.getLogger;

class BearerAuthenticator extends Authenticator<TokenValidator> {

    private static final Logger log = getLogger(BearerAuthenticator.class);

    BearerAuthenticator() {
        super(log, BEARER);
    }
}
