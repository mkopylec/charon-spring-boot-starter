package com.github.mkopylec.charon.test.specification

import spock.lang.Unroll

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
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.FOUND
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

abstract class ForwardingBasicSpec extends BasicSpec {

    @Unroll
    def "Should forward request using #method method by default"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK)

        when:
        sendRequest(method, '/default')

        then:
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(method, '/default')

        where:
        method << [GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS]
    }

    @Unroll
    def "Should forward request using #body body by default"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK)

        when:
        sendRequest(POST, '/default', body)

        then:
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(POST, '/default', body)

        where:
        body << ['', '  ', 'request body']
    }

    @Unroll
    def "Should forward response using #status status by default"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(status)

        when:
        def response = sendRequest(GET, '/default')

        then:
        assertThat(response)
                .hasStatus(status)

        where:
        status << [OK, CREATED, BAD_REQUEST, NOT_FOUND, INTERNAL_SERVER_ERROR]
    }

    // TODO Not working!
    def "Should not follow forwarded response redirect by default"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(FOUND, ['Location': 'https://github.com'])

        when:
        def response = sendRequest(GET, '/default')

        then:
        assertThat(response)
                .hasStatus(FOUND)
                .containsHeaders(['Location': 'https://github.com'])
    }

    // TODO more here, path-regex fail test
}
