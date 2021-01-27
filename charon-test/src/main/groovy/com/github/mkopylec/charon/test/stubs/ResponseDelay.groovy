package com.github.mkopylec.charon.test.stubs

enum ResponseDelay {

    REAL(0),
    TIMED_OUT(1)

    private int seconds

    ResponseDelay(int seconds) {
        this.seconds = seconds
    }

    protected int getSeconds() {
        return seconds
    }
}