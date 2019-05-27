package com.github.mkopylec.charon.test.specification

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

abstract class RequestHostHeaderRewriterBasicSpec extends BasicSpec {

    def "Should rewrite request 'Host' header"() {
        when:
        def response = sendRequest(GET, '/path/1/path')

        then:
        assertThat(response)
                .hasStatus(OK)
                .hasNoBody()
        assertThatServers(localhost8080)
                .haveReceivedRequest(GET, '/path/1/path', ['Host': 'localhost:8080'])
    }
}
