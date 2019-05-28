package com.github.mkopylec.charon.test.specification

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatOneOf
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

abstract class RequestHeadersRewritingBasicSpec extends BasicSpec {

    def "Should rewrite request headers by default"() {
        when:
        def response = sendRequest(GET, '/default', ['Host': 'example.com:666', 'X-Forwarded-For': 'another-example.com', 'TE': 'gzip'])

        then:
        assertThat(response)
                .hasStatus(OK)
                .hasNoBody()
        assertThatOneOf(localhost8080, localhost8081)
                .hasReceivedRequest(GET, '/default', ['X-Forwarded-For': 'another-example.com, example.com:666', 'X-Forwarded-Proto': 'http', 'X-Forwarded-Host': 'example.com', 'X-Forwarded-Port': '666', 'Connection': 'close'])
    }

    def "Should not rewrite request headers when proper interceptor is unset"() {
        when:
        def response = sendRequest(GET, '/request/headers', ['X-Forwarded-For': 'example.com', 'TE': 'gzip'])

        then:
        assertThat(response)
                .hasStatus(OK)
                .hasNoBody()
        assertThatOneOf(localhost8080, localhost8081)
                .hasReceivedRequest(GET, '/request/headers', ['X-Forwarded-For': 'example.com', 'TE': 'gzip'])
    }
}
