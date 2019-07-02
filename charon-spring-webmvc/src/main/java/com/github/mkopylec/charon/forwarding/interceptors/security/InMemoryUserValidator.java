package com.github.mkopylec.charon.forwarding.interceptors.security;

import java.util.ArrayList;
import java.util.List;

class InMemoryUserValidator implements UserValidator {

    private List<User> validUsers;

    InMemoryUserValidator() {
        validUsers = new ArrayList<>();
    }

    @Override
    public boolean validate(User user) {
        return validUsers.contains(user);
    }

    void addValidUser(User validUser) {
        validUsers.add(validUser);
    }
}
