package com.github.mkopylec.charon.test.specification

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.DELETE
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.HEAD
import static org.springframework.http.HttpMethod.OPTIONS
import static org.springframework.http.HttpMethod.PATCH
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpMethod.PUT
import static org.springframework.http.HttpStatus.OK

abstract class RequestForwardingBasicSpec extends BasicSpec {

    def "Should forward request with method by default"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK)

        when:
        http.sendRequest(method, '/default')

        then:
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(method, '/default')
                .haveNotReceivedRequest(['Content-Type': 'application/octet-stream'])

        where:
        method << [GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS]
    }

    def "Should forward request with body and set 'Content-Length' header by default"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK)

        when:
        http.sendRequest(POST, '/default', body)

        then:
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(POST, '/default', ['Content-Length': contentLength], body)

        where:
        body           | contentLength
        ''             | '0'
        '  '           | '2'
        'request body' | '12'
    }

    def "Should forward an SSL request by default"() {
        when:
        def response = http.sendRequest(GET, 'https://github.com')

        then:
        assertThat(response)
                .hasStatus(OK)
    }
}
