package com.github.mkopylec.charon.forwarding.interceptors.security;

import java.util.List;

import static java.util.Arrays.asList;

public class InMemoryTokenValidatorConfigurer extends TokenValidatorConfigurer<InMemoryTokenValidator> {

    private InMemoryTokenValidatorConfigurer() {
        super(new InMemoryTokenValidator());
    }

    public static InMemoryTokenValidatorConfigurer inMemoryTokenValidator() {
        return new InMemoryTokenValidatorConfigurer();
    }

    public InMemoryTokenValidatorConfigurer validTokens(String... validTokens) {
        configuredObject.setValidTokens(asList(validTokens));
        return this;
    }

    public InMemoryTokenValidatorConfigurer validTokens(List<String> validTokens) {
        configuredObject.setValidTokens(validTokens);
        return this;
    }
}
