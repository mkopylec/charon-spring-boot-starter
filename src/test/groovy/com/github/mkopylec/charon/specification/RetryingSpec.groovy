package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import org.springframework.test.context.TestPropertySource

import static com.github.mkopylec.charon.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR

@TestPropertySource(properties = ['charon.retrying.retry-on.client-http-error: true', 'charon.retrying.retry-on.server-http-error: false'])
class RetryingSpec extends BasicSpec {

    def "Should retry to proxy HTTP request when a retryable error occurs"() {
        given:
        stubDestinationResponse BAD_REQUEST

        when:
        def response = sendRequest GET, '/uri/1/path/1'

        then:
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .hasBody()
    }

    def "Should not retry to proxy HTTP request when a non-retryable error occurs"() {
        given:
        stubDestinationResponse INTERNAL_SERVER_ERROR

        when:
        def response = sendRequest GET, '/uri/1/path/1'

        then:
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .hasNoBody()
    }
}
