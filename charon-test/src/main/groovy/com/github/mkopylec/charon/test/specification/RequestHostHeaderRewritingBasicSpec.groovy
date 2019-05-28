package com.github.mkopylec.charon.test.specification

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatOneOf
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

abstract class RequestHostHeaderRewritingBasicSpec extends BasicSpec {

    def "Should rewrite request 'Host' header when proper interceptor is set"() {
        when:
        def response = sendRequest(GET, '/request/host/header', ['Host': 'example.com'])

        then:
        assertThat(response)
                .hasStatus(OK)
                .hasNoBody()
        assertThatOneOf(localhost8080)
                .hasReceivedRequest(GET, '/request/host/header', ['Host': 'localhost:8080'])
    }

    def "Should not rewrite request 'Host' header by default"() {
        when:
        def response = sendRequest(GET, '/default', ['Host': 'example.com'])

        then:
        assertThat(response)
                .hasStatus(OK)
                .hasNoBody()
        assertThatOneOf(localhost8080, localhost8081)
                .hasReceivedRequest(GET, '/default', ['Host': 'example.com'])
    }
}
