package com.github.mkopylec.charon.test.specification

import spock.lang.Unroll

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.FOUND
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

abstract class ResponseForwardingBasicSpec extends BasicSpec {

    @Unroll
    def "Should forward #status response status by default"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(status)

        when:
        def response = http.sendRequest(GET, '/default')

        then:
        assertThat(response)
                .hasStatus(status)

        where:
        status << [OK, CREATED, BAD_REQUEST, NOT_FOUND, INTERNAL_SERVER_ERROR]
    }

    def "Should not follow forwarded response redirect by default"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(FOUND, ['Location': 'https://github.com'])

        when:
        def response = http.sendRequest(GET, '/default')

        then:
        assertThat(response)
                .hasStatus(FOUND)
                .containsHeaders(['Location': 'https://github.com'])
    }

    @Unroll
    def "Should forward '#body' response body by default"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK, body)

        when:
        def response = http.sendRequest(GET, '/default')

        then:
        assertThat(response)
                .hasStatus(OK)
                .hasBody(body)

        where:
        body << [null, '  ', 'response body']
    }
}
