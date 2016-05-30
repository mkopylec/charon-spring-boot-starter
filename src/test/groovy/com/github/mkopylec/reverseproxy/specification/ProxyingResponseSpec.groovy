package com.github.mkopylec.reverseproxy.specification

import com.github.mkopylec.reverseproxy.BasicSpec
import spock.lang.Unroll

import static com.github.mkopylec.reverseproxy.TestController.SAMPLE_MESSAGE
import static com.github.mkopylec.reverseproxy.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.GET
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
        stubRequestWithResponse GET, '/path/1', status

        when:
        def response = sendRequest GET, '/uri/1/path/1'

        then:
        assertThat(response)
                .hasStatus(status)

        where:
        status << [OK, NO_CONTENT, FOUND, BAD_REQUEST, NOT_FOUND, INTERNAL_SERVER_ERROR]
    }

    @Unroll
    def "Should get proxied HTTP response with #responseHeaders when response headers are #receivedHeaders"() {
        given:
        stubRequestWithResponse GET, '/path/1', receivedHeaders

        when:
        def response = sendRequest GET, '/uri/1/path/1'

        then:
        assertThat(response)
                .containsHeaders(responseHeaders)

        where:
        receivedHeaders                                                | responseHeaders
        [:]                                                            | [:]
        ['Header-1': 'Value 1']                                        | ['Header-1': 'Value 1']
        ['Header-1': 'Value 1', 'Header-2': 'Value 2', 'Header-3': ''] | ['Header-1': 'Value 1', 'Header-2': 'Value 2']
    }

    @Unroll
    def "Should get proxied HTTP response with preserved body when response body is '#body'"() {
        given:
        stubRequestWithResponse GET, '/path/1', body

        when:
        def response = sendRequest GET, '/uri/1/path/1'

        then:
        assertThat(response)
                .hasBody(body)

        where:
        body << [null, '   ', 'Sample body']
    }

    def "Should not get proxied HTTP response when request URI is excluded from mappings"() {
        when:
        def response = sendRequest GET, '/not/mapped/uri'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedNoRequest()
        assertThat(response)
                .hasStatus(OK)
                .hasBody(SAMPLE_MESSAGE);
    }
}
