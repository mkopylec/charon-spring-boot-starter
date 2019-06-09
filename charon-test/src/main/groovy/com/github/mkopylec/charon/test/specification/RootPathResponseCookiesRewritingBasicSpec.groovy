package com.github.mkopylec.charon.test.specification

import spock.lang.Unroll

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

abstract class RootPathResponseCookiesRewritingBasicSpec extends BasicSpec {

    @Unroll
    def "Should rewrite response cookies from #incomingHeaders to #outgoingHeaders by default"() {
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
        incomingHeaders                             | outgoingHeaders
        ['Set-Cookie': 'cookie=value']              | ['Set-Cookie': 'cookie=value; Path=/']
        ['Set-Cookie': 'cookie=value; Path=/']      | ['Set-Cookie': 'cookie=value; Path=/']
        ['Set-Cookie': 'cookie=value; Path=/path']  | ['Set-Cookie': 'cookie=value; Path=/']
        ['Set-Cookie2': 'cookie=value']             | ['Set-Cookie2': 'cookie=value; Path=/']
        ['Set-Cookie2': 'cookie=value; Path=/']     | ['Set-Cookie2': 'cookie=value; Path=/']
        ['Set-Cookie2': 'cookie=value; Path=/path'] | ['Set-Cookie2': 'cookie=value; Path=/']
    }

    @Unroll
    def "Should not rewrite response cookies #incomingHeaders when proper interceptor is unset"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK, incomingHeaders)

        when:
        def response = http.sendRequest(GET, '/root/path/response/cookies')

        then:
        assertThat(response)
                .hasStatus(OK)
                .containsHeaders(outgoingHeaders)
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/root/path/response/cookies')

        where:
        incomingHeaders                             | outgoingHeaders
        ['Set-Cookie': 'cookie=value']              | ['Set-Cookie': 'cookie=value']
        ['Set-Cookie': 'cookie=value; Path=/']      | ['Set-Cookie': 'cookie=value; Path=/']
        ['Set-Cookie': 'cookie=value; Path=/path']  | ['Set-Cookie': 'cookie=value; Path=/path']
        ['Set-Cookie2': 'cookie=value']             | ['Set-Cookie2': 'cookie=value']
        ['Set-Cookie2': 'cookie=value; Path=/']     | ['Set-Cookie2': 'cookie=value; Path=/']
        ['Set-Cookie2': 'cookie=value; Path=/path'] | ['Set-Cookie2': 'cookie=value; Path=/path']
    }
}
