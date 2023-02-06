package com.github.mkopylec.charon.test.specification

import static com.github.mkopylec.charon.test.assertions.Assertions.assertThat
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatServers
import static com.github.mkopylec.charon.test.stubs.OutgoingServersStubs.outgoingServers
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNAUTHORIZED

abstract class BasicAuthenticationBasicSpec extends BasicSpec {

    def "Should forward request after successful basic authentication"() {
        given:
        outgoingServers(localhost8080, localhost8081)
                .stubResponse(OK)

        when:
        def response = http.sendRequest(GET, '/basic/authentication', ['Authorization': 'Basic dXNlcjpwYXNzd29yZA=='])

        then:
        assertThat(response)
                .hasStatus(OK)
    }

    def "Should not forward request after unsuccessful basic authentication for invalid 'Authorization' header"() {
        when:
        def response = http.sendRequest(GET, '/basic/authentication', headers)

        then:
        assertThat(response)
                .hasStatus(UNAUTHORIZED)
                .containsHeaders(['WWW-Authenticate': 'Basic realm="Charon security"'])
        assertThatServers(localhost8080, localhost8081)
                .haveNotReceivedRequest()

        where:
        headers << [
                [:],
                ['Authorization': ''],
                ['Authorization': 'Basic '],
                ['Authorization': 'Basic invalid-credentials'],
                ['Authorization': 'Basic dXNlcjo='],
                ['Authorization': 'Basic dXNlcjppbnZhbGlkLXBhc3N3b3Jk'],
                ['Authorization': 'Basic aW52YWxpZC11c2VyOnBhc3N3b3Jk']
        ]
    }
}
