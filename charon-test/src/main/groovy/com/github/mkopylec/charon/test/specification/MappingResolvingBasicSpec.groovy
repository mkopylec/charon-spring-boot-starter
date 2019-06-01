package com.github.mkopylec.charon.test.specification

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.OK

abstract class MappingResolvingBasicSpec extends BasicSpec {

    def "Should handle request locally when no request mapping could be found"() {
        when:
        def response = http.sendRequest(GET, '/no/mapping/found')

        then:
        assertThat(response)
                .hasStatus(OK)
                .hasBody('local endpoint response')
        assertThatServers(localhost8080, localhost8081)
                .haveNotReceivedRequest()
    }

    def "Should fail to forward request when multiple request mappings are found"() {
        when:
        def response = http.sendRequest(GET, '/multiple/mappings/found')

        then:
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .bodyContains("More than one request mapping matches /multiple/mappings/found incoming request. Matching mappings are 'multiple mappings found 1', 'multiple mappings found 2'")
        assertThatServers(localhost8080, localhost8081)
                .haveNotReceivedRequest()
    }

    void setup() {
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK)
    }
}
