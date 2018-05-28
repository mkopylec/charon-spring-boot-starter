package com.github.mkopylec.charon.assertions

import com.github.mkopylec.charon.application.TestForwardedRequestInterceptor

class InterceptorAssert {

    private TestForwardedRequestInterceptor actual

    protected InterceptorAssert(TestForwardedRequestInterceptor actual) {
        assert actual != null
        this.actual = actual
    }

    InterceptorAssert hasResponseDataContainingRequestData() {
        assert actual.requestData != null
        def requestData = actual.responseData.requestData
        assert requestData != null
        assert requestData.method == actual.requestData.method
        assert requestData.uri == actual.requestData.uri
        assert requestData.headers == actual.requestData.headers
        assert requestData.body == actual.requestData.body
        return this
    }
}
