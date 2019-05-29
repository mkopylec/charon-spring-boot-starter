package com.github.mkopylec.charon.test.assertions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import static org.apache.commons.lang3.StringUtils.isEmpty

class ResponseAssertion {

    private ResponseEntity response

    protected ResponseAssertion(ResponseEntity response) {
        assert response != null
        this.response = response
    }

    ResponseAssertion bodyContains(String bodyPart) {
        assert response.body != null
        assert response.body.toString().contains(bodyPart)
        return this
    }

    ResponseAssertion hasBody(String body) {
        assert response.body == body
        return this
    }

    ResponseAssertion hasNoBody() {
        assert isEmpty(response.body as CharSequence)
        return this
    }

    ResponseAssertion hasStatus(HttpStatus status) {
        assert response.statusCode == status
        return this
    }

    ResponseAssertion containsHeaders(Map<String, String> headers) {
        headers.each { k, v -> assert response.headers.get(k).join(', ') == v }
        return this
    }

    ResponseAssertion notContainsHeaders(List<String> headers) {
        headers.each { name -> assert !response.headers.containsKey(name) }
        return this
    }
}
