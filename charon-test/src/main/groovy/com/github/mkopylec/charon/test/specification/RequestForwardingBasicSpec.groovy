package com.github.mkopylec.charon.test.specification

import spock.lang.Unroll

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

    @Unroll
    def "Should forward request with #method method by default"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK)

        when:
        http.sendRequest(method, '/default')

        then:
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(method, '/default')

        where:
        method << [GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS]
    }

    @Unroll
    def "Should forward request with '#body' body by default"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK)

        when:
        http.sendRequest(POST, '/default', body)

        then:
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(POST, '/default', body)

        where:
        body << ['', '  ', 'request body']
    }

    // TODO more here, path-regex fail test
}
