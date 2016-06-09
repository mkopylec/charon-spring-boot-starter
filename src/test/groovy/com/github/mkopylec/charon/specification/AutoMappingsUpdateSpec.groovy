package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import org.springframework.test.context.TestPropertySource

import static com.github.mkopylec.charon.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.GET

@TestPropertySource(
        properties = ['charon.mappings-update.on-non-http-error: false', 'charon.mappings-update.interval-in-millis: 100']
)
class AutoMappingsUpdateSpec extends BasicSpec {

    def "Should automatically update destination mappings after a time interval"() {
        when:
        sendRequest GET, '/uri/6/path/6'

        then:
        assertThat(localhost8080)
                .haveReceivedRequest()
                .withMethodAndUri(GET, '/path/6')

        when:
        updateMappingDestinations('/uri/6', 'localhost:8081')
        sleep(150)
        sendRequest GET, '/uri/6/path/6'

        then:
        assertThat(localhost8081)
                .haveReceivedRequest()
                .withMethodAndUri(GET, '/path/6')
    }
}
