package com.github.mkopylec.charon.test.specification

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.OK

abstract class TimeoutBasicSpec extends BasicSpec {

    def "Should fail to forward request when outgoing server responds too long"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK, true)

        when:
        def response = http.sendRequest(GET, '/timeout')

        then:
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .bodyContains('Error executing request: Read timed out')
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/timeout')
    }
}
