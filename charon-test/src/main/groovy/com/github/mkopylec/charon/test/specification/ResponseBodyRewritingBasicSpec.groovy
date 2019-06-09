package com.github.mkopylec.charon.test.specification

import spock.lang.Unroll

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

abstract class ResponseBodyRewritingBasicSpec extends BasicSpec {

    @Unroll
    def "Should rewrite forwarded response body from '#originalBody' using custom interceptor"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK, originalBody)

        when:
        def response = http.sendRequest(GET, '/response/body/rewriting')

        then:
        assertThat(response)
                .hasBody('Rewritten response body')
                .containsHeaders(['Content-Length': '23'])
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/response/body/rewriting')

        where:
        originalBody << ['', 'original body']
    }
}
