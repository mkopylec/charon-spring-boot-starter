package com.github.mkopylec.charon.test.specification

import org.springframework.test.annotation.DirtiesContext

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatMetrics
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

abstract class LatencyMeteringBasicSpec extends BasicSpec {

    @DirtiesContext
    def "Should meter request forwarding latency when proper interceptor is set"() {
        when:
        def response = http.sendRequest(GET, '/latency/metering')

        then:
        assertThat(response)
                .hasStatus(OK)
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/latency/metering')
        assertThatMetrics()
                .haveCapturedLatency('charon.latency metering.latency')
    }

    def "Should not meter request forwarding latency by default"() {
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

    void setup() {
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK)
    }
}
