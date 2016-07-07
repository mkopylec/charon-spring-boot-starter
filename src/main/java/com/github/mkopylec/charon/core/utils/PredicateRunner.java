package com.github.mkopylec.charon.core.utils;

public class PredicateRunner {

    public static void runIfTrue(boolean condition, Runnable operation) {
        if (condition) {
            operation.run();
        }
    }
}
