package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

public class RetryingState {

    private static ThreadLocal<Integer> retryAttempts = new ThreadLocal<>();
    private static ThreadLocal<Boolean> nextAttempt = new ThreadLocal<>();
    private static ThreadLocal<Integer> retryerIndex = new ThreadLocal<>();

    public static int getRetryAttempts() {
        Integer attempt = retryAttempts.get();
        return attempt != null ? attempt : 0;
    }

    public static void setRetryerIndex(int index) {
        retryerIndex.set(index);
    }

    public static Integer getRetryerIndex() {
        return retryerIndex.get();
    }

    public static boolean isNextRetryAttempt() {
        return isTrue(nextAttempt.get());
    }

    public static void nextRetryAttemptUsed() {
        nextAttempt.set(false);
    }

    static void nextRetryAttempt() {
        int attempt = getRetryAttempts();
        retryAttempts.set(++attempt);
        nextAttempt.set(true);
    }

    static void clearRetryAttempts() {
        retryAttempts.remove();
        nextAttempt.remove();
        retryerIndex.remove();
    }
}
