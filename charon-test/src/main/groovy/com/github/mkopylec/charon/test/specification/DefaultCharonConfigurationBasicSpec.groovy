package com.github.mkopylec.charon.test.specification

import org.springframework.test.context.TestPropertySource

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

@TestPropertySource(properties = ['default-charon-configuration: true'])
abstract class DefaultCharonConfigurationBasicSpec extends BasicSpec {

    def "Should handle all requests locally when no custom Charon configuration is provided"() {
        when:
        def response = http.sendRequest(GET, '/default/charon/configuration')

        then:
        assertThat(response)
                .hasStatus(OK)
                .hasBody('default charon configuration response')
        assertThatServers(localhost8080, localhost8081)
                .haveNotReceivedRequest()
    }
}
