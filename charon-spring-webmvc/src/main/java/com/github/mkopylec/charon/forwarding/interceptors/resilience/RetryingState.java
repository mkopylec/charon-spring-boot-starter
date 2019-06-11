package com.github.mkopylec.charon.forwarding.interceptors.resilience;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

class RetryingState {

    private static ThreadLocal<Integer> retryAttempts = new ThreadLocal<>();
    private static ThreadLocal<Boolean> firstAttempt = new ThreadLocal<>();
    private static ThreadLocal<Boolean> nextAttempt = new ThreadLocal<>();
    private static ThreadLocal<Integer> afterRetryerIndex = new ThreadLocal<>();

    static boolean isFirstRetryAttempt() {
        return getRetryAttempts() == 1 && isTrue(firstAttempt.get());
    }

    static void firstRetryAttemptApplied() {
        firstAttempt.set(false);
    }

    static boolean isSucceedingRetryAttempt() {
        return getRetryAttempts() > 1 && isTrue(nextAttempt.get()) && afterRetryerIndex.get() != null;
    }

    static void succeedingRetryAttemptApplied() {
        nextAttempt.set(false);
    }

    static int getAfterRetryerIndex() {
        return afterRetryerIndex.get();
    }

    static void setAfterRetryerIndex(int index) {
        afterRetryerIndex.set(index);
    }

    static void nextRetryAttempt() {
        int attempt = getRetryAttempts();
        retryAttempts.set(++attempt);
        if (attempt == 1) {
            firstAttempt.set(true);
        }
        nextAttempt.set(true);
    }

    static void clearRetryAttempts() {
        retryAttempts.remove();
        firstAttempt.remove();
        nextAttempt.remove();
        afterRetryerIndex.remove();
    }

    private static int getRetryAttempts() {
        Integer attempt = retryAttempts.get();
        return attempt != null ? attempt : 0;
    }
}
