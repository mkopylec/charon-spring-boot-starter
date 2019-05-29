package com.github.mkopylec.charon.test.specification

import spock.lang.Unroll

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.OK

abstract class RegexRequestPathRewritingBasicSpec extends BasicSpec {

    @Unroll
    def "Should rewrite request path using regex from #incomingPath to #outgoingPath when proper interceptor is set"() {
        when:
        def response = sendRequest(GET, incomingPath)

        then:
        assertThat(response)
                .hasStatus(OK)
                .hasNoBody()
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, outgoingPath)

        where:
        incomingPath                             | outgoingPath
        '/regex/request/path/groups'             | '/group/template/path/groups'
        '/regex/request/path/groups/in/incoming' | '/no/group/template'
        '/regex/request/path/no/groups'          | '/no/group/template'
    }

    def "Should not rewrite request path using regex by default"() {
        when:
        def response = sendRequest(GET, '/default')

        then:
        assertThat(response)
                .hasStatus(OK)
                .hasNoBody()
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/default')
    }

    def "Should fail to rewrite request path using regex when outgoing path template has a placeholder but incoming path regex has no groups"() {
        when:
        def response = sendRequest(GET, '/regex/request/path/groups/in/outgoing')

        then:
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .bodyContains('No group with name <path>')
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedNoRequest()
    }

    def "Should fail to rewrite request path using regex when incoming path regex doesn't match incoming path"() {
        when:
        def response = sendRequest(GET, '/regex/request/path/no/match')

        then:
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .bodyContains('Incoming request path /regex/request/path/no/match does not match path rewriter regex pattern /no/match/(?<path>.*)')
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedNoRequest()
    }
}
