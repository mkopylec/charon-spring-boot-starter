package com.github.mkopylec.charon.test.assertions

class ExceptionAssertion {

    private Throwable throwable

    protected ExceptionAssertion(Throwable throwable) {
        assert throwable != null
        this.throwable = throwable
    }

    ExceptionAssertion hasMessage(String message) {
        assert throwable.message == message
        return this
    }
}
