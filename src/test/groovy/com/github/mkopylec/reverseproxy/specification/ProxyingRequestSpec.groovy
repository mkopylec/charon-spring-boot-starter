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
    def "Should proxy HTTP request preserving request method when method is #method"() {
        given:
        stubRequest method, '/path/1'

        when:
        sendRequest method, '/uri/1/path/1'

        then:
        assertThatRequestWasProxiedTo(localhost8080, localhost8081)
                .withMethodAndUri(method, '/path/1')

        where:
        method << [GET, POST, OPTIONS, DELETE, PUT, TRACE, HEAD]
    }

    @Unroll
    def "Should proxy HTTP request to #destinationUri when request uri is #requestUri"() {
        given:
        stubRequest GET, destinationUri

        when:
        sendRequest GET, requestUri

        then:
        assertThatRequestWasProxiedTo(localhost8080, localhost8081)
                .withMethodAndUri(GET, destinationUri)

        where:
        requestUri                       | destinationUri
        '/uri/1'                         | '/'
        '/uri/1?param1=1&param2'         | '/?param1=1&param2'
        '/uri/1/'                        | '/'
        '/uri/1/?param1=1&param2'        | '/?param1=1&param2'
        '/uri/1/path/1'                  | '/path/1'
        '/uri/1/path/1?param1=1&param2'  | '/path/1?param1=1&param2'
        '/uri/1/path/1/'                 | '/path/1/'
        '/uri/1/path/1/?param1=1&param2' | '/path/1/?param1=1&param2'
    }

    @Unroll
    def "Should proxy HTTP request with headers #destinationHeaders when request headers are #requestHeaders"() {
        given:
        stubRequest GET, '/path/1', destinationHeaders

        when:
        sendRequest GET, '/uri/1/path/1', requestHeaders

        then:
        assertThatRequestWasProxiedTo(localhost8080, localhost8081)
                .withHeaders(destinationHeaders)

        where:
        requestHeaders                                                 | destinationHeaders
        [:]                                                            | [:]
        ['Header-1': 'Value 1']                                        | ['Header-1': 'Value 1']
        ['Header-1': 'Value 1', 'Header-2': 'Value 2', 'Header-3': ''] | ['Header-1': 'Value 1', 'Header-2': 'Value 2']
    }

    @Unroll
    def "Should proxy HTTP request preserving request body when body is '#body'"() {
        given:
        stubRequest POST, '/path/1', body

        when:
        sendRequest POST, '/uri/1/path/1', [:], body

        then:
        assertThatRequestWasProxiedTo(localhost8080, localhost8081)
                .withBody(body)

        where:
        body << ['', '   ', 'Sample body']
    }
}
