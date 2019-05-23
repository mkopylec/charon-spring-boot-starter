package com.github.mkopylec.charon.test.specification

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

class AfterServerNameRequestHeadersRewriterBasicSpec extends BasicSpec {

    def "Should rewrite request headers after rewriting its server name"() {
        when:
        def response = sendRequest(GET, '/path/1/path', ['TE': 'compress'])

        then:
        assertThat(response)
                .hasStatus(OK)
                .hasNoBody()
        outgoingServers.verifyRequest(GET, '/path/1/path', ['Host': 'localhost'])
    }
}
