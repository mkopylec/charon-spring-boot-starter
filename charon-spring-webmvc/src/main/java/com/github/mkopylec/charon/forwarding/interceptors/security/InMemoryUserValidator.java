package com.github.mkopylec.charon.forwarding.interceptors.security;

import java.util.ArrayList;
import java.util.List;

class InMemoryUserValidator implements UserValidator {

    private List<User> validUsers;

    InMemoryUserValidator() {
        validUsers = new ArrayList<>();
    }

    @Override
    public boolean validate(String userName, String password) {
        User user = new User(userName, password);
        return validUsers.contains(user);
    }

    void addValidUser(User validUser) {
        validUsers.add(validUser);
    }
}
