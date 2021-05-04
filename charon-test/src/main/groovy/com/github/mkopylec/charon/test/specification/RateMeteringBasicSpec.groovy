package com.github.mkopylec.charon.test.specification

import org.springframework.test.annotation.DirtiesContext
import spock.lang.Unroll

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatMetrics
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY
import static org.springframework.http.HttpStatus.OK

abstract class RateMeteringBasicSpec extends BasicSpec {

    @Unroll
    @DirtiesContext
    def "Should meter request forwarding rate for #responseStatus response status when proper interceptor is set"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(responseStatus)

        when:
        def response = http.sendRequest(GET, '/rate/metering')

        then:
        assertThat(response)
                .hasStatus(responseStatus)
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/rate/metering')
        assertThatMetrics()
                .haveCapturedRate("charon.rate metering.response.status.${responseStatus.value()}")

        where:
        responseStatus << [OK, MOVED_PERMANENTLY, BAD_REQUEST, INTERNAL_SERVER_ERROR]
    }

    @DirtiesContext
    def "Should meter request forwarding rate for exception occurrence when proper interceptor is set"() {
        when:
        def response = http.sendRequest(GET, '/exception/rate/metering')

        then:
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
        assertThatMetrics()
                .haveCapturedRate('charon.exception rate metering.response.exception.runtimeexception')
    }

    def "Should not meter request forwarding rate by default"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK)

        when:
        def response = http.sendRequest(GET, '/default')

        then:
        assertThat(response)
                .hasStatus(OK)
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/default')
        assertThatMetrics()
                .haveCapturedNothing()
    }
}
