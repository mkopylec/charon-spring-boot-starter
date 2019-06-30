package com.github.mkopylec.charon.forwarding.interceptors.security;

public class InMemoryUserValidatorConfigurer extends UserValidatorConfigurer<InMemoryUserValidator> {

    private InMemoryUserValidatorConfigurer() {
        super(new InMemoryUserValidator());
    }

    public static InMemoryUserValidatorConfigurer inMemoryUserValidator() {
        return new InMemoryUserValidatorConfigurer();
    }

    public InMemoryUserValidatorConfigurer validUser(String name, String password) {
        User user = new User(name, password);
        configuredObject.addValidUser(user);
        return this;
    }
}
