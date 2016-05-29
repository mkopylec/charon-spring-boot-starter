package com.github.mkopylec.reverseproxy.assertions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ResponseAssert {

    private ResponseEntity actual

    protected ResponseAssert(ResponseEntity actual) {
        assert actual != null
        this.actual = actual
    }

    ResponseAssert hasBody(String body) {
        assert actual.body == body
        return this
    }

    ResponseAssert hasStatus(HttpStatus status) {
        assert actual.statusCode == status
        return this
    }
}
