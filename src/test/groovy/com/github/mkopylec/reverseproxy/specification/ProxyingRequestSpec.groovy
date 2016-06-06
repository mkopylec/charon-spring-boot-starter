package com.github.mkopylec.reverseproxy.specification

import com.github.mkopylec.reverseproxy.BasicSpec
import spock.lang.Unroll

import static com.github.mkopylec.reverseproxy.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.DELETE
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.HEAD
import static org.springframework.http.HttpMethod.OPTIONS
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpMethod.PUT
import static org.springframework.http.HttpMethod.TRACE
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR

class ProxyingRequestSpec extends BasicSpec {

    @Unroll
    def "Should proxy HTTP request preserving request method when method is #method"() {
        when:
        sendRequest method, '/uri/1/path/1'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedRequest()
                .withMethodAndUri(method, '/path/1')
                .withoutBody()

        where:
        method << [GET, POST, OPTIONS, DELETE, PUT, TRACE, HEAD]
    }

    @Unroll
    def "Should proxy HTTP request to #destinationUri when request uri is #requestUri"() {
        when:
        sendRequest GET, requestUri

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedRequest()
                .withMethodAndUri(GET, destinationUri)
                .withoutBody()

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
        when:
        sendRequest GET, '/uri/1/path/1', requestHeaders

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedRequest()
                .withMethodAndUri(GET, '/path/1')
                .withHeaders(destinationHeaders)
                .withHeaders(['X-Forwarded-Proto': 'http', 'X-Forwarded-Host': 'localhost', 'X-Forwarded-Port': port])
                .withoutBody()

        where:
        requestHeaders                                                 | destinationHeaders
        [:]                                                            | ['X-Forwarded-For': '127.0.0.1']
        ['Header-1': 'Value 1']                                        | ['Header-1': 'Value 1', 'X-Forwarded-For': '127.0.0.1']
        ['Header-1': 'Value 1', 'Header-2': 'Value 2', 'Header-3': ''] | ['Header-1': 'Value 1', 'Header-2': 'Value 2', 'X-Forwarded-For': '127.0.0.1']
        ['X-Forwarded-For': '172.10.89.11']                            | ['X-Forwarded-For': '172.10.89.11, 127.0.0.1']
    }

    @Unroll
    def "Should proxy HTTP request preserving request body when body is '#body'"() {
        when:
        sendRequest POST, '/uri/1/path/1', [:], body

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedRequest()
                .withMethodAndUri(POST, '/path/1')
                .withBody(body)

        where:
        body << ['', '   ', 'Sample body']
    }

    def "Should proxy HTTP request without stripping mapped path when path stripping is disabled"() {
        when:
        sendRequest GET, '/uri/2/path/2'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedRequest()
                .withMethodAndUri(GET, "$contextPath/uri/2/path/2")
                .withoutBody()
    }

    def "Should fail to proxy HTTP request when there are multiple mappings for request URI"() {
        when:
        def response = sendRequest GET, '/uri/3/path/3'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedNoRequest()
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
    }

    def "Should fail to proxy HTTP request when destination URL cannot be created"() {
        when:
        def response = sendRequest GET, '/uri/4/path/4'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedNoRequest()
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
    }

    def "Should update destination mappings when on non HTTP error while sending request to destination"() {
        when:
        def response = sendRequest GET, '/uri/5/path/5'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedNoRequest()
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)

        when:
        updateMappingDestinations('/uri/5', 'localhost:8080', 'localhost:8081')
        sendRequest GET, '/uri/5/path/5'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedRequest()
                .withMethodAndUri(GET, '/path/5')
    }
}
