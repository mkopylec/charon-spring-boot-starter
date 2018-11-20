package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import spock.lang.Unroll

import static com.github.mkopylec.charon.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.DELETE
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.HEAD
import static org.springframework.http.HttpMethod.OPTIONS
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpMethod.PUT
import static org.springframework.http.HttpMethod.TRACE
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.OK

abstract class ProxyingRequestSpec extends BasicSpec {

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
        '/uri/8'                         | '/'
        '/uri/8/a'                       | '/'
        '/uri/8/a/b'                     | '/b'
        '/uri/8/c'                       | '/c'
    }

    @Unroll
    def "Should proxy HTTP request with headers #destinationHeaders when request headers are #requestHeaders including X-Forwarded headers"() {
        when:
        sendRequest GET, '/uri/1/path/1', requestHeaders

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedRequest()
                .withMethodAndUri(GET, '/path/1')
                .withHeaders(destinationHeaders)
                .withHeaders(['X-Forwarded-Proto': 'http', 'X-Forwarded-Host': 'localhost', 'X-Forwarded-Port': port.toString()])
                .withoutBody()

        where:
        requestHeaders                                                 | destinationHeaders
        [:]                                                            | ['X-Forwarded-For': '127.0.0.1']
        ['Header-1': 'Value 1']                                        | ['Header-1': 'Value 1', 'X-Forwarded-For': '127.0.0.1']
        ['Header-1': 'Value 1', 'Header-2': 'Value 2', 'Header-3': ''] | ['Header-1': 'Value 1', 'Header-2': 'Value 2', 'X-Forwarded-For': '127.0.0.1']
        ['X-Forwarded-For': '172.10.89.11']                            | ['X-Forwarded-For': '172.10.89.11, 127.0.0.1']
        ['X-Forwarded-For': '172.10.89.11']                            | ['X-Forwarded-For': '172.10.89.11, 127.0.0.1']
    }

    @Unroll
    def "Should proxy HTTP request with headers #destinationHeaders when request headers are #requestHeaders"() {
        when:
        sendRequest GET, '/uri/9/path/9', requestHeaders

        then:
        assertThat(localhost8080)
                .haveReceivedRequest()
                .withMethodAndUri(GET, '/path/9')
                .withHeaders(destinationHeaders)
                .withoutHeaders(removedHeaders)
                .withoutBody()

        where:
        requestHeaders                            | destinationHeaders         | removedHeaders
        [:]                                       | ['Host': 'localhost:8080'] | []
        ['TE': 'compress']                        | ['Host': 'localhost:8080'] | ['TE']
        ['Host': 'example.com', 'TE': 'compress'] | ['Host': 'localhost:8080'] | ['TE']
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

    def "Should proxy HTTP request without stripping mapped path when path stripping is disabled and multiple paths found"() {
        when:
        sendRequest GET, '/uri/3/path/3'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedRequest()
                .withMethodAndUri(GET, "$contextPath/uri/3/path/3")
                .withoutBody()
    }

    def "Should proxy HTTP request rewriting its path"() {
        when:
        sendRequest GET, '/uri/10/path/10'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedRequest()
                .withMethodAndUri(GET, '/rewritten')
                .withoutBody()
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

    def "Should proxy HTTP request when asynchronous mode is enabled"() {
        when:
        sendRequest GET, '/uri/7/path/7'
        sleep(300)

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedRequest()
                .withMethodAndUri(GET, '/path/7')
                .withoutBody()
    }

    def "Should proxy HTTPS request"() {
        when:
        def response = sendRequest(GET, '/mkopylec')

        then:
        assertThat(response)
                .hasStatus(OK)
    }
}
