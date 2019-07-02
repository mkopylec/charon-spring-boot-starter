package com.github.mkopylec.charon.test.specification

import spock.lang.Unroll

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNAUTHORIZED

abstract class BearerAuthenticationBasicSpec extends BasicSpec {

    def "Should forward request after successful bearer authentication"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK)

        when:
        def response = http.sendRequest(GET, '/bearer/authentication', ['Authorization': 'Bearer token'])

        then:
        assertThat(response)
                .hasStatus(OK)
    }

    @Unroll
    def "Should not forward request after unsuccessful bearer authentication for #headers request headers"() {
        when:
        def response = http.sendRequest(GET, '/bearer/authentication', headers)

        then:
        assertThat(response)
                .hasStatus(UNAUTHORIZED)
                .containsHeaders(['WWW-Authenticate': 'Bearer realm="Charon security"'])
        assertThatServers(localhost8080, localhost8081)
                .haveNotReceivedRequest()

        where:
        headers << [
                [:],
                ['Authorization': ''],
                ['Authorization': 'Bearer '],
                ['Authorization': 'Bearer invalid-token']
        ]
    }
}
