package com.github.mkopylec.charon.test.specification

import org.springframework.test.annotation.DirtiesContext
import spock.lang.Ignore

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatMetrics
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR

abstract class CircuitBreakingBasicSpec extends BasicSpec {

    @Ignore
    // TODO Unignore after https://github.com/resilience4j/resilience4j/issues/384 is done
    @DirtiesContext
    def "Should break circuit while forwarding request on HTTP 5xx response when proper interceptor is set"() {
        given:
        outgoingServers(localhost8080)
                .stubResponse(INTERNAL_SERVER_ERROR, 'response body', 2)

        when:
        http.sendRequest(GET, '/circuit/breaking')
        def response = http.sendRequest(GET, '/circuit/breaking')

        then:
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .hasBody("CircuitBreaker 'circuit breaking' is OPEN and does not permit further calls")
        assertThatServers(localhost8080)
                .haveReceivedRequest(GET, '/circuit/breaking')
        assertThatMetrics()
                .haveCaptured('charon.circuit breaking.circuit-breaking.buffered-calls')
                .haveCaptured('charon.circuit breaking.circuit-breaking.calls')
                .haveCaptured('charon.circuit breaking.circuit-breaking.max-buffered-calls')
                .haveCaptured('charon.circuit breaking.circuit-breaking.state')
    }

    @DirtiesContext
    def "Should break circuit while forwarding request on exception when proper interceptor is set"() {
        when:
        http.sendRequest(GET, '/exception/circuit/breaking')
        def response = http.sendRequest(GET, '/exception/circuit/breaking')

        then:
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .bodyContains("CircuitBreaker 'exception circuit breaking' is OPEN and does not permit further calls")
        assertThatServers(localhost8080, localhost8081)
                .haveNotReceivedRequest()
        assertThatMetrics()
                .haveCaptured('charon.exception circuit breaking.circuit-breaking.buffered-calls')
                .haveCaptured('charon.exception circuit breaking.circuit-breaking.calls')
                .haveCaptured('charon.exception circuit breaking.circuit-breaking.max-buffered-calls')
                .haveCaptured('charon.exception circuit breaking.circuit-breaking.state')
    }

    @DirtiesContext
    def "Should execute circuit breaker fallback while forwarding request when proper interceptor is set"() {
        when:
        http.sendRequest(GET, '/fallback/circuit/breaking')
        def response = http.sendRequest(GET, '/fallback/circuit/breaking')

        then:
        assertThat(response)
                .hasStatus(CREATED)
        assertThatServers(localhost8080, localhost8081)
                .haveNotReceivedRequest()
        assertThatMetrics()
                .haveCaptured('charon.fallback circuit breaking.circuit-breaking.buffered-calls')
                .haveCaptured('charon.fallback circuit breaking.circuit-breaking.calls')
                .haveCaptured('charon.fallback circuit breaking.circuit-breaking.max-buffered-calls')
                .haveCaptured('charon.fallback circuit breaking.circuit-breaking.state')
    }

    @DirtiesContext
    def "Should not break circuit while forwarding request on HTTP 4xx response when proper interceptor is set"() {
        given:
        outgoingServers(localhost8080)
                .stubResponse(BAD_REQUEST, 2)

        when:
        http.sendRequest(GET, '/circuit/breaking')
        def response = http.sendRequest(GET, '/circuit/breaking')

        then:
        assertThat(response)
                .hasStatus(BAD_REQUEST)
        assertThatServers(localhost8080)
                .haveReceivedRequest(GET, '/circuit/breaking', 2)
        assertThatMetrics()
                .haveCaptured('charon.circuit breaking.circuit-breaking.buffered-calls')
                .haveCaptured('charon.circuit breaking.circuit-breaking.calls')
                .haveCaptured('charon.circuit breaking.circuit-breaking.max-buffered-calls')
                .haveCaptured('charon.circuit breaking.circuit-breaking.state')
    }
}
