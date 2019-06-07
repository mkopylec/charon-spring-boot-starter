package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

public class RetryingState {

    private static ThreadLocal<Integer> retryAttempts = new ThreadLocal<>();
    private static ThreadLocal<Boolean> nextAttempt = new ThreadLocal<>();
    private static ThreadLocal<Integer> afterRetryerIndex = new ThreadLocal<>();

    public static boolean isFirstRetryAttempt() {
        return getRetryAttempts() == 1;
    }

    public static boolean isSucceedingRetryAttempt() {
        return getRetryAttempts() > 1 && isTrue(nextAttempt.get()) && afterRetryerIndex.get() != null;
    }

    public static void setAfterRetryerIndex(int index) {
        afterRetryerIndex.set(index);
    }

    public static int getAfterRetryerIndex() {
        return afterRetryerIndex.get();
    }

    public static void succeedingRetryAttemptApplied() {
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
        afterRetryerIndex.remove();
    }

    private static int getRetryAttempts() {
        Integer attempt = retryAttempts.get();
        return attempt != null ? attempt : 0;
    }
}
