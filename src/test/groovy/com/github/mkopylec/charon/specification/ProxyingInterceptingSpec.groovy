package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import com.github.mkopylec.charon.application.TestForwardedRequestInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource
import spock.lang.Unroll

import static com.github.mkopylec.charon.application.TestForwardedRequestInterceptor.INTERCEPTED_AUTHORIZATION
import static com.github.mkopylec.charon.application.TestForwardedRequestInterceptor.INTERCEPTED_BODY
import static com.github.mkopylec.charon.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.DELETE
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.ACCEPTED
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK

@TestPropertySource(properties = ['test.interceptors-enabled: true'])
class ProxyingInterceptingSpec extends BasicSpec {

    @Autowired
    private TestForwardedRequestInterceptor interceptor

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

    def "Should change HTTP response while proxying HTTP request"() {
        given:
        stubDestinationResponse OK

        when:
        def response = sendRequest GET, '/uri/1/sync'

        then:
        assertThat(response)
                .hasStatus(CREATED)
                .hasBody(INTERCEPTED_BODY)

        assertThat(interceptor)
                .hasInterceptedRequest()
                .hasInterceptedResponse()
                .withRequestInResponse()
                .withRequestMethod(DELETE)
                .withRequestUri('/uri/1/sync')
                .withRequestHeader('Authorization', INTERCEPTED_AUTHORIZATION)
                .withRequestHeader('X-Forwarded-For', '127.0.0.1')
                .withResponseStatus(CREATED)
                .withResponseBody(INTERCEPTED_BODY)
    }

    def "Should not change HTTP response while proxying HTTP request when asynchronous mode is enabled"() {
        given:
        stubDestinationResponse OK

        when:
        def response = sendRequest GET, '/uri/7/async'

        then:
        assertThat(response)
                .hasStatus(ACCEPTED)
                .hasNoBody()
    }
}
