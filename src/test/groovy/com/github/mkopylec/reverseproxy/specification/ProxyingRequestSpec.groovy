package com.github.mkopylec.reverseproxy.specification

import com.github.mkopylec.reverseproxy.BasicSpec
import spock.lang.Unroll

import static com.github.mkopylec.reverseproxy.assertions.Assertions.assertThatRequestWasProxiedTo
import static org.springframework.http.HttpMethod.DELETE
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.HEAD
import static org.springframework.http.HttpMethod.OPTIONS
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpMethod.PUT
import static org.springframework.http.HttpMethod.TRACE

class ProxyingRequestSpec extends BasicSpec {

    @Unroll
    def "Should proxy HTTP request when request method is #method"() {
        given:
        stubRequest(method, '/path/1')

        when:
        sendRequest '/uri/1/path/1', method

        then:
        assertThatRequestWasProxiedTo(localhost8080, localhost8081)
                .withMethodAndUri(method, '/path/1')

        where:
        method << [GET, POST, OPTIONS, DELETE, PUT, TRACE, HEAD]
    }
}
