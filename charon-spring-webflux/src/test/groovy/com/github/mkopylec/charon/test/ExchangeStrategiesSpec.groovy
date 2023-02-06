package com.github.mkopylec.charon.test

import com.github.mkopylec.charon.test.specification.BasicSpec

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.http.HttpStatus.OK

class ExchangeStrategiesSpec extends BasicSpec {

    def "Should fail to forward request when response size is larger than exchange strategy allows"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK, 'to large response body')

        when:
        def response = http.sendRequest(GET, '/exchange/strategies')

        then:
        assertThat(response)
                .hasStatus(INTERNAL_SERVER_ERROR)
        assertThatServers(localhost8080, localhost8081)
                .haveReceivedRequest(GET, '/exchange/strategies')
    }
}
