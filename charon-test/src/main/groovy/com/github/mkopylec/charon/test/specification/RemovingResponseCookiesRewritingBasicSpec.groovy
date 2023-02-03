package com.github.mkopylec.charon.test.specification

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

abstract class RemovingResponseCookiesRewritingBasicSpec extends BasicSpec {

    def "Should rewrite response cookies by removing headers when proper interceptor is set"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK, incomingHeaders)

        when:
        def response = http.sendRequest(GET, '/removing/response/cookies')

        then:
        assertThat(response)
                .hasStatus(OK)
                .notContainsHeaders(removedHeaders)
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/removing/response/cookies')

        where:
        incomingHeaders                         | removedHeaders
        ['Set-Cookie': 'cookie=value; Path=/']  | 'Set-Cookie'
        ['Set-Cookie2': 'cookie=value; Path=/'] | 'Set-Cookie2'
    }

    def "Should not remove response cookies by default"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK, incomingHeaders)

        when:
        def response = http.sendRequest(GET, '/default')

        then:
        assertThat(response)
                .hasStatus(OK)
                .containsHeaders(outgoingHeaders)
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/default')

        where:
        incomingHeaders                         | outgoingHeaders
        ['Set-Cookie': 'cookie=value; Path=/']  | ['Set-Cookie': 'cookie=value; Path=/']
        ['Set-Cookie2': 'cookie=value; Path=/'] | ['Set-Cookie2': 'cookie=value; Path=/']
    }
}
