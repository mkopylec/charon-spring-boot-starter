package com.github.mkopylec.charon.specification

import com.github.mkopylec.charon.BasicSpec
import com.github.mkopylec.charon.application.TestMetricsReporter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext

import static com.github.mkopylec.charon.assertions.Assertions.assertThat
import static org.springframework.http.HttpMethod.GET

class MetricsSpec extends BasicSpec {

    @Autowired
    private TestMetricsReporter metricsReporter

    @DirtiesContext
    def "Should capture metrics while proxying HTTP request after a time interval"() {
        when:
        sendRequest GET, '/uri/1/path/1'
        sleep(1000)

        then:
        assertThat(metricsReporter)
                .hasCapturedMetrics()
    }
}
