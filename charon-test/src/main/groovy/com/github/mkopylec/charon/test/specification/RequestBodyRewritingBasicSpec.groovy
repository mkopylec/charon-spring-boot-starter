package com.github.mkopylec.charon.test.specification

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpStatus.OK

abstract class RequestBodyRewritingBasicSpec extends BasicSpec {

    def "Should rewrite forwarded request body using custom interceptor"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK)

        when:
        def response = http.sendRequest(POST, '/request/body/rewriting', originalBody)

        then:
        assertThat(response)
                .hasStatus(OK)
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(POST, '/request/body/rewriting', ['Content-Length': '22'], 'Rewritten request body')

        where:
        originalBody << ['', 'original body']
    }
}
