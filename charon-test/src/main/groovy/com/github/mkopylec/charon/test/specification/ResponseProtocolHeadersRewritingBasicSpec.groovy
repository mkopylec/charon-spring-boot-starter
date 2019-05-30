package com.github.mkopylec.charon.test.specification

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

abstract class ResponseProtocolHeadersRewritingBasicSpec extends BasicSpec {

    def "Should rewrite response protocol headers by default"() {
        given:
        def headers = ['Transfer-Encoding'        : 'gzip',
                       'Connection'               : 'keep-alive',
                       'Public-Key-Pins'          : 'pin-sha256="abc"',
                       'Server'                   : 'Apache/2.4.1 (Unix)',
                       'Strict-Transport-Security': 'max-age=31536000']
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK, headers)

        when:
        def response = sendRequest(GET, '/default')

        then:
        assertThat(response)
                .hasStatus(OK)
                .notContainsHeaders('Transfer-Encoding', 'Connection', 'Public-Key-Pins', 'Server', 'Strict-Transport-Security')
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/default')
    }

    def "Should not rewrite response protocol headers when proper interceptor is unset"() {
        given:
        def headers = ['Transfer-Encoding'        : 'gzip',
                       'Connection'               : 'keep-alive',
                       'Public-Key-Pins'          : 'pin-sha256="abc"',
                       'Server'                   : 'Apache/2.4.1 (Unix)',
                       'Strict-Transport-Security': 'max-age=31536000']
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK, headers)

        when:
        def response = sendRequest(GET, '/response/protocol/headers')

        then:
        assertThat(response)
                .hasStatus(OK)
                .containsHeaders(headers)
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/response/protocol/headers')
    }
}
