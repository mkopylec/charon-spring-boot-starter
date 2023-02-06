package com.github.mkopylec.charon.forwarding.interceptors.security;

import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;
import static reactor.core.publisher.Mono.just;

class InMemoryTokenValidator implements TokenValidator {

    private List<String> validTokens;

    InMemoryTokenValidator() {
        validTokens = emptyList();
    }

    @Override
    public Mono<Boolean> validate(String token) {
        return just(validTokens.contains(token));
    }

    void setValidTokens(List<String> validTokens) {
        this.validTokens = emptyIfNull(validTokens);
    }
}
