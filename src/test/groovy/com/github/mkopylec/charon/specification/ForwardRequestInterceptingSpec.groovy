package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import org.springframework.test.context.TestPropertySource
import spock.lang.Unroll

import static com.github.mkopylec.charon.application.TestForwardedRequestInterceptor.INTERCEPTED_AUTHORIZATION
import static com.github.mkopylec.charon.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.DELETE
import static org.springframework.http.HttpMethod.GET

@TestPropertySource(properties = ['test.forwarded-request-interceptor-enabled: true'])
class ForwardRequestInterceptingSpec extends BasicSpec {

    @Unroll
    def "Should change forwarded request while proxying HTTP request when URI is #requestUri"() {
        when:
        sendRequest GET, requestUri
        sleep(300)

        then:
        assertThat(localhost8080, localhost8081)
                .haveReceivedRequest()
                .withMethodAndUri(DELETE, destinationUri)
                .withHeaders(['X-Forwarded-For': '127.0.0.1', 'Authorization': INTERCEPTED_AUTHORIZATION])
                .withoutBody()

        where:
        requestUri     | destinationUri
        '/uri/1/sync'  | '/sync'
        '/uri/7/async' | '/async'
    }
}
