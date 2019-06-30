package com.github.mkopylec.charon.forwarding.interceptors.security;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;

class InMemoryTokenValidator implements TokenValidator {

    private List<String> validTokens;

    InMemoryTokenValidator() {
        validTokens = emptyList();
    }

    @Override
    public boolean validate(String token) {
        return validTokens.contains(token);
    }

    void setValidTokens(List<String> validTokens) {
        this.validTokens = emptyIfNull(validTokens);
    }
}
