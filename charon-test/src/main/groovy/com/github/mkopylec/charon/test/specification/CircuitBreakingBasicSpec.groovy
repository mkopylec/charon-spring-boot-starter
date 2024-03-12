package com.github.mkopylec.charon.test.specification

import org.springframework.test.annotation.DirtiesContext

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatMetrics
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR

abstract class CircuitBreakingBasicSpec extends BasicSpec {

    @DirtiesContext
    // TODO Resilience4j features support resetting but charon uses internal registries. Try to find out how to reset them to speed up the tests.
    // TODO After upgrading to spring-boot 3.2.3 the Tomcat server cannot be stopped normally on my local machine (mac). It is forced to stop after 10s. Logs from Tomcat show mockserver-netty version...
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
                .bodyContains("CircuitBreaker 'circuit breaking' is OPEN and does not permit further calls")
        assertThatServers(localhost8080)
                .haveReceivedRequest(GET, '/circuit/breaking')
        assertThatMetrics()
                .haveCaptured('charon.circuit breaking.circuit-breaking.buffered-calls')
                .haveCaptured('charon.circuit breaking.circuit-breaking.calls')
                .haveCaptured('charon.circuit breaking.circuit-breaking.state')
                .haveCaptured('charon.circuit breaking.circuit-breaking.failure-rate')
                .haveCaptured('charon.circuit breaking.circuit-breaking.slow-calls-rate')
                .haveCaptured('charon.circuit breaking.circuit-breaking.slow-calls')
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
                .haveCaptured('charon.exception circuit breaking.circuit-breaking.state')
                .haveCaptured('charon.exception circuit breaking.circuit-breaking.failure-rate')
                .haveCaptured('charon.exception circuit breaking.circuit-breaking.slow-calls-rate')
                .haveCaptured('charon.exception circuit breaking.circuit-breaking.slow-calls')
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
                .haveCaptured('charon.fallback circuit breaking.circuit-breaking.state')
                .haveCaptured('charon.fallback circuit breaking.circuit-breaking.failure-rate')
                .haveCaptured('charon.fallback circuit breaking.circuit-breaking.slow-calls-rate')
                .haveCaptured('charon.fallback circuit breaking.circuit-breaking.slow-calls')
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
                .haveCaptured('charon.circuit breaking.circuit-breaking.state')
                .haveCaptured('charon.circuit breaking.circuit-breaking.failure-rate')
                .haveCaptured('charon.circuit breaking.circuit-breaking.slow-calls-rate')
                .haveCaptured('charon.circuit breaking.circuit-breaking.slow-calls')
    }
}
