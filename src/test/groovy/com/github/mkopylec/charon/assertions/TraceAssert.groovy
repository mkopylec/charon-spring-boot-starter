package com.github.mkopylec.charon.assertions

import com.github.mkopylec.charon.application.TestTraceInterceptor

class TraceAssert {

    private TestTraceInterceptor actual

    protected TraceAssert(TestTraceInterceptor actual) {
        assert actual != null
        this.actual = actual
    }

    TraceAssert hasCapturedReceivedRequest() {
        assert actual.isRequestReceivedCaptured()
        return this
    }

    TraceAssert hasCapturedForwardStartWithMapping() {
        assert actual.isForwardStartWithMappingCaptured()
        return this
    }

    TraceAssert hasCapturedForwardStartWithNoMapping() {
        assert actual.isForwardStartWithNoMappingCaptured()
        return this
    }

    TraceAssert hasCapturedForwardCompletion() {
        assert actual.isForwardCompleteCaptured()
        return this
    }

    TraceAssert hasNotCapturedForwardCompletion() {
        assert !actual.isForwardCompleteCaptured()
        return this
    }
}
