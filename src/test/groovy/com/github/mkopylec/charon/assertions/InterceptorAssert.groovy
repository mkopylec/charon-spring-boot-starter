package com.github.mkopylec.charon.assertions

import com.github.mkopylec.charon.application.TestForwardedRequestInterceptor
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus


class InterceptorAssert {

    private TestForwardedRequestInterceptor actual

    protected InterceptorAssert(TestForwardedRequestInterceptor actual) {
        assert actual != null
        this.actual = actual
    }

    InterceptorAssert hasInterceptedRequest() {
        assert actual.getInterceptedRequest() != null
        return this
    }

    InterceptorAssert hasInterceptedResponse() {
        assert actual.getInterceptedResponse() != null
        return this
    }

    InterceptorAssert withRequestInResponse() {
        assert actual.getInterceptedResponse().requestData == actual.getInterceptedRequest()
        return this
    }

    InterceptorAssert withRequestMethod(HttpMethod method) {
        assert actual.getInterceptedRequest().method == method
        return this
    }

    InterceptorAssert withRequestUri(String uri) {
        assert actual.getInterceptedRequest().uri == uri;
        return this
    }

    InterceptorAssert withRequestHeader(String name, String value) {
        assert actual.getInterceptedRequest().headers != null &&
                actual.getInterceptedRequest().headers.get(name) != null &&
                actual.getInterceptedRequest().headers.get(name).contains(value)
        return this
    }

    InterceptorAssert withResponseStatus(HttpStatus status) {
        assert actual.getInterceptedResponse().status == status
        return this
    }

    InterceptorAssert withResponseBody(String body) {
        assert Arrays.equals(actual.getInterceptedResponse().body, body.bytes)
        return this
    }
}
