package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import com.github.mkopylec.charon.application.GraphiteServerMock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource

import static com.github.mkopylec.charon.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.GET

@TestPropertySource(properties = ['management.metrics.export.graphite.enabled: true'])
class MetricsSpec extends BasicSpec {

    @Autowired
    private GraphiteServerMock graphiteServer

    @DirtiesContext
    def "Should capture metrics while proxying HTTP request after a time interval"() {
        when:
        sendRequest GET, '/uri/1/path/1'
        sleep(1000)

        then:
        assertThat(graphiteServer)
                .hasCapturedMetrics()
    }
}
