package com.github.mkopylec.charon.core.utils;

public class PredicateRunner {

    public static void runIfTrue(boolean condition, Runnable operation) {
        if (condition) {
            operation.run();
        }
    }

//    public static <T> T runIfTrue(boolean condition, Supplier<T> operation) {
//        if (condition) {
//            return operation.get();
//        }
//        return null;
//    }
}
