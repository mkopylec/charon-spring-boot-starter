package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import com.github.mkopylec.charon.application.TestTraceInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource

import static com.github.mkopylec.charon.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.GET

@TestPropertySource(properties = ['charon.tracing.enabled: true'])
class TracingSpec extends BasicSpec {

    @Autowired
    private TestTraceInterceptor traceInterceptor

    @DirtiesContext
    def "Should capture trace while proxying HTTP request when matching mapping exists"() {
        when:
        sendRequest GET, '/uri/1/path/1'

        then:
        assertThat(traceInterceptor)
                .hasCapturedReceivedRequest()
                .hasCapturedForwardStartWithMapping()
                .hasCapturedForwardCompletion()
                .hasUnchangeableTraceId()
    }

    @DirtiesContext
    def "Should capture trace while proxying HTTP request when matching mapping does not exist"() {
        when:
        sendRequest GET, '/not/mapped/uri'

        then:
        assertThat(traceInterceptor)
                .hasCapturedReceivedRequest()
                .hasCapturedForwardStartWithNoMapping()
                .hasNotCapturedForwardCompletion()
                .hasUnchangeableTraceId()
    }
}
