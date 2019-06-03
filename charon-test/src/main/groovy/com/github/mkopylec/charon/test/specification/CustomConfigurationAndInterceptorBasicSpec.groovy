package com.github.mkopylec.charon.test.specification

import spock.lang.Unroll

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNAUTHORIZED

abstract class CustomConfigurationAndInterceptorBasicSpec extends BasicSpec {

    @Unroll
    def "Should rewrite forwarded response status to #status when incoming request path is #path using custom configuration and interceptor"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK)

        when:
        def response = http.sendRequest(GET, path)

        then:
        assertThat(response)
                .hasStatus(status)
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, path)

        where:
        path                            | status
        '/default/custom/configuration' | FORBIDDEN
        '/mapping/custom/configuration' | UNAUTHORIZED
    }

    @Unroll
    def "Should rewrite forwarded response body to '#body' when incoming request path is #path using custom configuration and interceptor"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK)

        when:
        def response = http.sendRequest(GET, path)

        then:
        assertThat(response)
                .hasBody(body)
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, path)

        where:
        path                            | body
        '/default/custom/configuration' | '403 FORBIDDEN'
        '/mapping/custom/configuration' | '401 UNAUTHORIZED'
    }
}
