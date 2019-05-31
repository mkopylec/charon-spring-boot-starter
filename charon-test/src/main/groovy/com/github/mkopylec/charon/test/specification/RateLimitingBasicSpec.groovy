package com.github.mkopylec.charon.test.specification

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatMetrics
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.OK

abstract class RateLimitingBasicSpec extends BasicSpec {

    def "Should limit request forwarding rate when proper interceptor is set"() {
        when:
        http.sendRequest(GET, '/rate/limiting')
        def response = http.sendRequest(GET, '/rate/limiting')

        then:
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
                .bodyContains("RateLimiter 'rate limiting' does not permit further calls")
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/rate/limiting')
        assertThatMetrics()
                .haveCaptured('charon.rate limiting.rate-limiting.available-permissions')
                .haveCaptured('charon.rate limiting.rate-limiting.waiting-threads')
    }

    void setup() {
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK)
    }
}
