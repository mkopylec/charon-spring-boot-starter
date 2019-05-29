package com.github.mkopylec.charon.test.specification

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.ACCEPTED
import static org.springframework.http.HttpStatus.OK

abstract class AsynchronousForwardingBasicSpec extends BasicSpec {

    def "Should asynchronously forward request when proper interceptor is set"() {
        when:
        def response = sendRequest(GET, '/asynchronous/forwarding')

        then:
        assertThat(response)
                .hasStatus(ACCEPTED)
                .hasNoBody()
        sleep(300)
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/asynchronous/forwarding')
    }

    def "Should synchronously forward request by default"() {
        when:
        def response = sendRequest(GET, '/default')

        then:
        assertThat(response)
                .hasStatus(OK)
                .hasNoBody()
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/default')
    }
}
