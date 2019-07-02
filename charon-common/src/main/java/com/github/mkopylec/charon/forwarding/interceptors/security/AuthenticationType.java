package com.github.mkopylec.charon.forwarding.interceptors.security;

enum AuthenticationType {

    BASIC("Basic"), BEARER("Bearer");

    private String string;

    AuthenticationType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
