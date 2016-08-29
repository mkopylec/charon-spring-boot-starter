package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import com.github.mkopylec.charon.application.TestTraceInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import spock.lang.Unroll

import static com.github.mkopylec.charon.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.GET

@TestPropertySource(properties = ['test.trace-interceptor-enabled: true'])
class TracingSpec extends BasicSpec {

    @Autowired
    private TestTraceInterceptor traceInterceptor

    @Unroll
    @DirtiesContext
    def "Should capture trace while proxying HTTP request when matching mapping exists and URI is #uri"() {
        when:
        sendRequest GET, uri
        sleep(300)

        then:
        assertThat(traceInterceptor)
                .hasCapturedReceivedRequest()
                .hasCapturedForwardStart()
                .hasNotCapturedForwardError()
                .hasCapturedForwardCompletion()
                .hasUnchangeableTraceId()

        where:
        uri << ['/uri/1/sync', '/uri/7/async']
    }

    @DirtiesContext
    def "Should capture trace while proxying HTTP request when matching mapping does not exist"() {
        when:
        sendRequest GET, '/not/mapped/uri'

        then:
        assertThat(traceInterceptor)
                .hasCapturedReceivedRequest()
                .hasCapturedNoMappingFound()
                .hasNotCapturedForwardError()
                .hasNotCapturedForwardCompletion()
                .hasUnchangeableTraceId()
    }

    @Unroll
    @DirtiesContext
    def "Should capture trace while proxying HTTP request when an error occurs and URi is #uri"() {
        when:
        sendRequest GET, uri
        sleep(300)

        then:
        assertThat(traceInterceptor)
                .hasCapturedReceivedRequest()
                .hasCapturedForwardStart()
                .hasCapturedForwardError()
                .hasNotCapturedForwardCompletion()
                .hasUnchangeableTraceId()

        where:
        uri << ['/uri/5/sync', '/uri/6/async']
    }
}
