package com.github.mkopylec.charon.test.specification

import org.springframework.test.annotation.DirtiesContext

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatMetrics
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.OK

abstract class RetryingBasicSpec extends BasicSpec {

    @DirtiesContext
    def "Should successfully retry request forwarding on HTTP 5xx response when proper interceptor is set"() {
        given:
        outgoingServers(localhost8080)
                .stubResponse(INTERNAL_SERVER_ERROR, 2)
        outgoingServers(localhost8080)
                .stubResponse(OK)

        when:
        def response = http.sendRequest(GET, '/retrying')

        then:
        assertThat(response)
                .hasStatus(OK)
        assertThatServers(localhost8080)
                .haveReceivedRequest(GET, '/retrying', 3)
        assertThatMetrics()
                .haveCaptured('charon.retrying.retrying.calls')
    }

    @DirtiesContext
    def "Should unsuccessfully retry request forwarding on HTTP 5xx response when proper interceptor is set"() {
        given:
        outgoingServers(localhost8080)
                .stubResponse(INTERNAL_SERVER_ERROR, 'response body', 3)

        when:
        def response = http.sendRequest(GET, '/retrying')

        then:
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .hasBody('response body')
        assertThatServers(localhost8080)
                .haveReceivedRequest(GET, '/retrying', 3)
        assertThatMetrics()
                .haveCaptured('charon.retrying.retrying.calls')
    }

    @DirtiesContext
    def "Should unsuccessfully retry request forwarding on exception when proper interceptor is set"() {
        when:
        def response = http.sendRequest(GET, '/exception/retrying')

        then:
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .bodyContains('non-existing.host')
        assertThatMetrics()
                .haveCaptured('charon.exception retrying.retrying.calls')
    }

    @DirtiesContext
    def "Should not retry request forwarding on HTTP 4xx response when proper interceptor is set"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(BAD_REQUEST)

        when:
        def response = http.sendRequest(GET, '/retrying')

        then:
        assertThat(response)
                .hasStatus(BAD_REQUEST)
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/retrying')
        assertThatMetrics()
                .haveCaptured('charon.retrying.retrying.calls')
    }
}
