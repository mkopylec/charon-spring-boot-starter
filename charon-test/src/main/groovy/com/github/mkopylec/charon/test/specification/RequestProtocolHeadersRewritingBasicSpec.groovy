package com.github.mkopylec.charon.test.specification

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

abstract class RequestProtocolHeadersRewritingBasicSpec extends BasicSpec {

    def "Should rewrite request protocol headers by default"() {
        when:
        def response = http.sendRequest(GET, '/default', ['TE': 'gzip'])

        then:
        assertThat(response)
                .hasStatus(OK)
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/default', ['Connection': 'close'])
                .haveNotReceivedRequest(['TE': 'gzip'])
    }

    def "Should not rewrite request protocol headers when proper interceptor is unset"() {
        when:
        def response = http.sendRequest(GET, '/request/protocol/headers', ['TE': 'gzip'])

        then:
        assertThat(response)
                .hasStatus(OK)
        assertThatServers(localhost8080, localhost8081)
                .haveNotReceivedRequest(['Connection': 'close'])
                .haveReceivedRequest(GET, '/request/protocol/headers', ['TE': 'gzip'])
    }

    void setup() {
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK)
    }
}
