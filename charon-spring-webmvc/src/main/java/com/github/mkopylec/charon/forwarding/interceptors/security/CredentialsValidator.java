package com.github.mkopylec.charon.forwarding.interceptors.security;

import com.github.mkopylec.charon.configuration.Valid;

interface CredentialsValidator extends Valid {

    boolean validate(String credentials);
}
