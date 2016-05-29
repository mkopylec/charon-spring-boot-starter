package com.github.mkopylec.reverseproxy.specification

import com.github.mkopylec.reverseproxy.BasicSpec

import static com.github.mkopylec.reverseproxy.TestController.SAMPLE_MESSAGE
import static com.github.mkopylec.reverseproxy.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

class ProxyingResponseSpec extends BasicSpec {

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
