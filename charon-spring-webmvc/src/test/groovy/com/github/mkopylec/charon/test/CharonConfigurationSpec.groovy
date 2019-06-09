package com.github.mkopylec.charon.test

import spock.lang.Specification
import spock.lang.Unroll

import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration
import static com.github.mkopylec.charon.configuration.RequestMappingConfigurer.requestMapping
import static com.github.mkopylec.charon.forwarding.RestTemplateConfigurer.restTemplate
import static com.github.mkopylec.charon.forwarding.TimeoutConfigurer.timeout
import static com.github.mkopylec.charon.forwarding.interceptors.latency.LatencyMeterConfigurer.latencyMeter
import static com.github.mkopylec.charon.forwarding.interceptors.log.ForwardingLoggerConfigurer.forwardingLogger
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestServerNameRewriterConfigurer.requestServerNameRewriter
import static com.github.mkopylec.charon.test.assertions.Assertions.assertThatException
import static java.time.Duration.ofMillis

class CharonConfigurationSpec extends Specification {

    def "Should properly validate request mapping"() {
        when:
        charonConfiguration()
                .add(requestMapping(null))

        then:
        def exception = thrown(IllegalArgumentException)
        assertThatException(exception)
                .hasMessage('No request forwarding name set')
    }

    def "Should properly validate latency meter"() {
        when:
        charonConfiguration()
                .set(latencyMeter().meterRegistry(null))

        then:
        def exception = thrown(IllegalArgumentException)
        assertThatException(exception)
                .hasMessage('No meter registry set')
    }

    @Unroll
    def "Should properly validate forwarding logger resulting in '#message' error"() {
        when:
        charonConfiguration()
                .set(logger)

        then:
        def exception = thrown(IllegalArgumentException)
        assertThatException(exception)
                .hasMessage(message)

        where:
        logger                                           | message
        forwardingLogger().successLogLevel(null)         | 'No success log level set'
        forwardingLogger().clientErrorLogLevel(null)     | 'No client error log level set'
        forwardingLogger().serverErrorLogLevel(null)     | 'No server error log level set'
        forwardingLogger().unexpectedErrorLogLevel(null) | 'No unexpected error log level set'
    }

    def "Should properly validate request server name rewriter"() {
        when:
        charonConfiguration()
                .set(requestServerNameRewriter().outgoingServers())

        then:
        def exception = thrown(IllegalArgumentException)
        assertThatException(exception)
                .hasMessage('No outgoing servers set')
    }

    @Unroll
    def "Should properly validate rest template resulting in '#message' error"() {
        when:
        charonConfiguration()
                .set(restTemplate().set(timeOut))

        then:
        def exception = thrown(IllegalArgumentException)
        assertThatException(exception)
                .hasMessage(message)

        where:
        timeOut                            | message
        timeout().connection(null)         | 'No connection timeout set'
        timeout().connection(ofMillis(-1)) | 'Invalid connection timeout value: -1 ms'
        timeout().read(null)               | 'No read timeout set'
        timeout().read(ofMillis(-1))       | 'Invalid read timeout value: -1 ms'
        timeout().write(null)              | 'No write timeout set'
        timeout().write(ofMillis(-1))      | 'Invalid write timeout value: -1 ms'
    }
}
