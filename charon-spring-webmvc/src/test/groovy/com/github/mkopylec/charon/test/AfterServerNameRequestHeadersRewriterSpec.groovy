package com.github.mkopylec.charon.test

import com.github.mkopylec.charon.test.specification.BasicSpec

import static org.springframework.http.HttpMethod.GET

class AfterServerNameRequestHeadersRewriterSpec extends BasicSpec {

    def "Should rewrite request headers after rewriting its server name"() {
        when:
        def x = true
        def response = sendRequest(GET, '/test')

        then:
        outgoingServers.verifyRequest(GET, '/test', [:])
    }
}
