package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import spock.lang.Unroll

import static com.github.mkopylec.charon.application.TestController.SAMPLE_HEADER
import static com.github.mkopylec.charon.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpStatus.ACCEPTED
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.FOUND
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

class ProxyingResponseSpec extends BasicSpec {

    @Unroll
    def "Should get proxied HTTP response with preserved status when response status is #status"() {
        given:
        stubDestinationResponse status

        when:
        def response = sendRequest GET, '/uri/1/path/1'

        then:
        assertThat(response)
                .hasStatus(status)
                .hasNoBody()

        where:
        status << [OK, NO_CONTENT, FOUND, BAD_REQUEST, NOT_FOUND, INTERNAL_SERVER_ERROR]
    }

    @Unroll
    def "Should get proxied HTTP response with #responseHeaders when response headers are #receivedHeaders"() {
        given:
        stubDestinationResponse receivedHeaders

        when:
        def response = sendRequest GET, '/uri/1/path/1'

        then:
        assertThat(response)
                .hasStatus(OK)
                .containsHeaders(responseHeaders)
                .notContainsHeaders(removedHeaders)
                .hasNoBody()

        where:
        receivedHeaders                                                | responseHeaders                                                | removedHeaders
        ['Header-1': 'Value 1']                                        | ['Header-1': 'Value 1']                                        | []
        ['Header-1': 'Value 1', 'Header-2': 'Value 2', 'Header-3': ''] | ['Header-1': 'Value 1', 'Header-2': 'Value 2', 'Header-3': ''] | []
        ['Transfer-Encoding': 'chunked']                               | [:]                                                            | ['Transfer-Encoding']
        ['Connection': 'close']                                        | [:]                                                            | ['Connection']
        ['Public-Key-Pins': 'pin-sha256']                              | [:]                                                            | ['Public-Key-Pins']
        ['Server': 'Apache/2.4.1 (Unix)']                              | [:]                                                            | ['Server']
        ['Strict-Transport-Security': 'max-age=3600']                  | [:]                                                            | ['Strict-Transport-Security']
    }

    def "Should get proxied HTTP response with preserved headers when response status indicates error"() {
        given:
        stubDestinationResponse INTERNAL_SERVER_ERROR, ['Header-1': 'Value 1']

        when:
        def response = sendRequest GET, '/uri/1/path/1'

        then:
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .containsHeaders(['Header-1': 'Value 1'])
                .hasNoBody()
    }

    @Unroll
    def "Should get proxied HTTP response with preserved body when response body is '#body'"() {
        given:
        stubDestinationResponse body

        when:
        def response = sendRequest GET, '/uri/1/path/1'

        then:
        assertThat(response)
                .hasStatus(OK)
                .hasBody(body)

        where:
        body << [null, '   ', 'Sample body']
    }

    def "Should get proxied HTTP response with preserved body when response status indicates error"() {
        given:
        stubDestinationResponse BAD_REQUEST, 'Sample body'

        when:
        def response = sendRequest GET, '/uri/1/path/1'

        then:
        assertThat(response)
                .hasStatus(BAD_REQUEST)
                .hasBody('Sample body')
    }

    def "Should not proxy HTTP request when request URI is excluded from mappings"() {
        given:
        def header = SAMPLE_HEADER.toString()

        when:
        def response = sendRequest POST, '/not/mapped/uri', [(header): 'value'], 'sample body'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedNoRequest()
        assertThat(response)
                .hasStatus(OK)
                .hasBody('sample body')
                .containsHeaders([(header): 'value'])
    }

    def "Should get non-proxied HTTP response with 202 (accepted) HTTP status when asynchronous mode is enabled"() {
        when:
        def response = sendRequest GET, '/uri/7/path/7'

        then:
        assertThat(response)
                .hasStatus(ACCEPTED)
                .hasNoBody()
    }

    def "Should fail to proxy HTTP request when a timeout occurs"() {
        given:
        stubDestinationResponse true

        when:
        def response = sendRequest GET, '/uri/1/path/1'

        then:
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
    }
}
