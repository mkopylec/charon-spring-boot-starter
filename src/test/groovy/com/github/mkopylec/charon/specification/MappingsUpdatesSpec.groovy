package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import spock.lang.Unroll

import static com.github.mkopylec.charon.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.NOT_FOUND

@TestPropertySource(properties = ['test.mappings-provider-enabled: true'])
class MappingsUpdatesSpec extends BasicSpec {

    @DirtiesContext
    def "Should update mappings when mappings provider forces it"() {
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

    @DirtiesContext
    def "Should fail to update mappings when new mappings have duplicated paths"() {
        given:
        addMapping('new proxy 6', '/uri/new/6', 'localhost:8080', 'localhost:8081')
        addMapping('new proxy 7', '/uri/new/6', 'localhost:8080', 'localhost:8081')

        when:
        def response = sendRequest GET, '/uri/5/path/5'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedNoRequest()
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .bodyContains('Duplicated destination paths in mappings')
    }

    @Unroll
    @DirtiesContext
    def "Should fail to update mappings when new mapping name is '#mappingName'"() {
        given:
        addMapping(mappingName, '/uri/new/3', 'localhost:8080', 'localhost:8081')

        when:
        def response = sendRequest GET, '/uri/5/path/5'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedNoRequest()
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .bodyContains('Empty name for mapping')

        where:
        mappingName << [null, '', '  ']
    }

    @DirtiesContext
    def "Should fail to update mappings when new mapping has no destinations"() {
        given:
        addMapping('new proxy 2', '/uri/new/2')

        when:
        def response = sendRequest GET, '/uri/5/path/5'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedNoRequest()
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .bodyContains('No destination hosts for mapping')
    }

    @Unroll
    @DirtiesContext
    def "Should fail to update mappings when new mapping has '#destinationHost' destination host"() {
        given:
        addMapping('new proxy 4', '/uri/new/4', 'localhost:8080', destinationHost)

        when:
        def response = sendRequest GET, '/uri/5/path/5'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedNoRequest()
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .bodyContains('Empty destination for mapping')

        where:
        destinationHost << [null, '', '  ']
    }

    @Unroll
    @DirtiesContext
    def "Should fail to update mappings when new mapping has '#path' path"() {
        given:
        addMapping('new proxy 5', path, 'localhost:8080', 'localhost:8081')

        when:
        def response = sendRequest GET, '/uri/5/path/5'

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedNoRequest()
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .bodyContains('No destination path for mapping')

        where:
        path << [null, '', '  ']
    }
}
