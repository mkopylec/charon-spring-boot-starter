package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import org.springframework.test.context.TestPropertySource
import spock.lang.Unroll

import static com.github.mkopylec.charon.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.OK

@TestPropertySource(properties = ['charon.hystrix.enabled: true'])
class HystrixSpec extends BasicSpec {

    def "Should fail to proxy HTTP request when a timeout occurs"() {
        given:
        stubDestinationResponse true

        when:
        def response = sendRequest GET, '/uri/1/path/1'

        then:
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
    }

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
        status << [OK, BAD_REQUEST]
    }
}
