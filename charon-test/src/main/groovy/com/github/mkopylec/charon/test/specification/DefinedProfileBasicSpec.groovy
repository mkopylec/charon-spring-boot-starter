package com.github.mkopylec.charon.test.specification

import org.springframework.test.context.ActiveProfiles

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpStatus.OK

@ActiveProfiles('defined')
abstract class DefinedProfileBasicSpec extends BasicSpec {

    def "Should add new mapping for 'defined' Spring Boot profile"() {
        given:
        outgoingServers(localhost8081)
                .stubResponse(OK)

        when:
        def response = http.sendRequest(GET, '/new/mapping')

        then:
        assertThat(response)
                .hasStatus(OK)
        assertThatServers(localhost8081)
                .haveReceivedRequest(GET, '/new/mapping')
    }

    def "Should overwrite existing request mapping for 'defined' Spring Boot profile"() {
        given:
        outgoingServers(localhost8081)
                .stubResponse(OK)

        when:
        def response = http.sendRequest(POST, '/overwritten/request/body/rewriting', 'original body')

        then:
        assertThat(response)
                .hasStatus(OK)
        assertThatServers(localhost8081)
                .haveReceivedRequest(POST, '/overwritten/request/body/rewriting', 'original body')
    }

    def "Should update existing request mapping for 'defined' Spring Boot profile"() {
        given:
        outgoingServers(localhost8081)
                .stubResponse(OK, 'original body')

        when:
        def response = http.sendRequest(GET, '/updated/response/body/rewriting')

        then:
        assertThat(response)
                .hasBody('Rewritten response body')
        assertThatServers(localhost8081)
                .haveReceivedRequest(GET, '/updated/response/body/rewriting')
    }
}
