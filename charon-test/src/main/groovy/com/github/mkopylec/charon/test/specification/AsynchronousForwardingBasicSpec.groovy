package com.github.mkopylec.charon.test.specification

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.ACCEPTED
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.OK

abstract class AsynchronousForwardingBasicSpec extends BasicSpec {

    def "Should asynchronously forward request and receive response status when proper interceptor is set"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(status, 'response body')

        when:
        def response = http.sendRequest(GET, '/asynchronous/forwarding')

        then:
        assertThat(response)
                .hasStatus(ACCEPTED)
                .hasNoBody()
        sleep(300)
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/asynchronous/forwarding')

        where:
        status << [OK, BAD_REQUEST, INTERNAL_SERVER_ERROR]
    }

    def "Should asynchronously forward request and receive exception when proper interceptor is set"() {
        when:
        def response = http.sendRequest(GET, '/exception/asynchronous/forwarding')

        then:
        assertThat(response)
                .hasStatus(ACCEPTED)
                .hasNoBody()
        sleep(300)
    }

    def "Should synchronously forward request by default"() {
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
    }
}
