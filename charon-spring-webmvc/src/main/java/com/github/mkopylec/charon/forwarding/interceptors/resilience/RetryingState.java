package com.github.mkopylec.charon.forwarding.interceptors.resilience;

class RetryingState {

    private static ThreadLocal<State> state = new ThreadLocal<>();

    static State getRetryState() {
        if (state.get() == null) {
            state.set(new State());
        }
        return state.get();
    }

    static void clearRetryState() {
        state.remove();
    }

    static class State {

        private int retryAttempts;
        private boolean firstAttempt;
        private boolean nextAttempt;
        private Integer afterRetryerIndex;

        boolean isFirstRetryAttempt() {
            return retryAttempts == 1 && firstAttempt;
        }

        void firstRetryAttemptApplied() {
            firstAttempt = false;
        }

        boolean isSucceedingRetryAttempt() {
            return retryAttempts > 1 && nextAttempt && afterRetryerIndex != null;
        }

        void succeedingRetryAttemptApplied() {
            nextAttempt = false;
        }

        int getAfterRetryerIndex() {
            return afterRetryerIndex;
        }

        void setAfterRetryerIndex(int index) {
            afterRetryerIndex = index;
        }

        void nextRetryAttempt() {
            retryAttempts++;
            if (retryAttempts == 1) {
                firstAttempt = true;
            }
            nextAttempt = true;
        }
    }
}
