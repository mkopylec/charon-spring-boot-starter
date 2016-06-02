package com.github.mkopylec.reverseproxy.specification

import com.github.mkopylec.reverseproxy.BasicSpec

import static com.github.mkopylec.reverseproxy.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR

class DestinationMappingsSpec extends BasicSpec {

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
