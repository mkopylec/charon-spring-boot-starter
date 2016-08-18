package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import org.springframework.test.annotation.DirtiesContext

import static com.github.mkopylec.charon.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.NOT_FOUND

class MappingsUpdatesSpec extends BasicSpec {

    @DirtiesContext
    def "Should update destination mappings when mappings provider forces it"() {
        when:
        def response = sendRequest GET, '/uri/new/1/path/new'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedNoRequest()
        assertThat(response)
                .hasStatus(NOT_FOUND)

        when:
        addMapping('new proxy 1', '/uri/new/1', 'localhost:8080', 'localhost:8081')
        sendRequest GET, '/uri/new/1/path/new'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedRequest()
                .withMethodAndUri(GET, '/path/new')
    }
}
