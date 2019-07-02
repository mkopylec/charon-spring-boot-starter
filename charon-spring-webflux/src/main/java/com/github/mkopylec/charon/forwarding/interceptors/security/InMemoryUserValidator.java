package com.github.mkopylec.charon.forwarding.interceptors.security;

import java.util.ArrayList;
import java.util.List;

import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.just;

class InMemoryUserValidator implements UserValidator {

    private List<User> validUsers;

    InMemoryUserValidator() {
        validUsers = new ArrayList<>();
    }

    @Override
    public Mono<Boolean> validate(User user) {
        return just(validUsers.contains(user));
    }

    void addValidUser(User validUser) {
        validUsers.add(validUser);
    }
}
