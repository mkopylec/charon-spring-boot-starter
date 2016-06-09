package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import spock.lang.Unroll

import static com.github.mkopylec.charon.application.TestController.SAMPLE_MESSAGE
import static com.github.mkopylec.charon.assertions.Assertions.assertThat
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
                .hasNoBody()

        where:
        receivedHeaders                                                | responseHeaders
        [:]                                                            | [:]
        ['Header-1': 'Value 1']                                        | ['Header-1': 'Value 1']
        ['Header-1': 'Value 1', 'Header-2': 'Value 2', 'Header-3': ''] | ['Header-1': 'Value 1', 'Header-2': 'Value 2']
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
