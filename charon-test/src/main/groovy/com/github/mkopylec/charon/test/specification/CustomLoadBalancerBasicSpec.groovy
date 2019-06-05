package com.github.mkopylec.charon.test.specification

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK

abstract class CustomLoadBalancerBasicSpec extends BasicSpec {

    def "Should forward request using custom load balancer"() {
        given:
        outgoingServers(localhost8080)
                .stubResponse(OK)
        when:
        http.sendRequest(GET, '/custom/load/balancer')

        then:
        assertThatServers(localhost8080)
                .haveReceivedRequest(GET, '/custom/load/balancer')
    }
}
