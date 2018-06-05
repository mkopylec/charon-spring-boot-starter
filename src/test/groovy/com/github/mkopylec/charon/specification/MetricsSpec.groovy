package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import com.github.mkopylec.charon.application.TestMetricsReporter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource

import static com.github.mkopylec.charon.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.GET

@TestPropertySource(properties = ['management.metrics.export.graphite.enabled: true'])
class MetricsSpec extends BasicSpec {

    @Autowired
    private TestMetricsReporter metricsReporter

    @DirtiesContext
    def "Should capture metrics while proxying HTTP request after a time interval"() {
        when:
        sendRequest GET, '/uri/1/path/1'

        then:
        assertThat(metricsReporter)
                .hasCapturedMetrics('charon.proxy 1')
    }
}
