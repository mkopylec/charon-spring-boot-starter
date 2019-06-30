package com.github.mkopylec.charon.test

import spock.lang.Specification
import spock.lang.Unroll

import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration
import static com.github.mkopylec.charon.configuration.RequestMappingConfigurer.requestMapping
import static com.github.mkopylec.charon.forwarding.RestTemplateConfigurer.restTemplate
import static com.github.mkopylec.charon.forwarding.TimeoutConfigurer.timeout
import static com.github.mkopylec.charon.forwarding.interceptors.log.ForwardingLoggerConfigurer.forwardingLogger
import static com.github.mkopylec.charon.forwarding.interceptors.metrics.LatencyMeterConfigurer.latencyMeter
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestServerNameRewriterConfigurer.requestServerNameRewriter
import static com.github.mkopylec.charon.forwarding.interceptors.security.BasicAuthenticatorConfigurer.basicAuthenticator
import static com.github.mkopylec.charon.forwarding.interceptors.security.BearerAuthenticatorConfigurer.bearerAuthenticator
import static com.github.mkopylec.charon.forwarding.interceptors.security.InMemoryTokenValidatorConfigurer.inMemoryTokenValidator
import static com.github.mkopylec.charon.forwarding.interceptors.security.InMemoryUserValidatorConfigurer.inMemoryUserValidator
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

    @Unroll
    def "Should properly validate basic authenticator resulting in '#message' error"() {
        when:
        charonConfiguration()
                .set(authenticator)

        then:
        def exception = thrown(IllegalArgumentException)
        assertThatException(exception)
                .hasMessage(message)

        where:
        authenticator                                                           | message
        basicAuthenticator()                                                    | 'No credentials validator set'
        basicAuthenticator().userValidator(inMemoryUserValidator()).realm(null) | 'No authentication realm set'
        basicAuthenticator().userValidator(inMemoryUserValidator()).realm(' ')  | 'No authentication realm set'
    }

    @Unroll
    def "Should properly validate bearer authenticator resulting in '#message' error"() {
        when:
        charonConfiguration()
                .set(authenticator)

        then:
        def exception = thrown(IllegalArgumentException)
        assertThatException(exception)
                .hasMessage(message)

        where:
        authenticator                                                              | message
        bearerAuthenticator()                                                      | 'No credentials validator set'
        bearerAuthenticator().tokenValidator(inMemoryTokenValidator()).realm(null) | 'No authentication realm set'
        bearerAuthenticator().tokenValidator(inMemoryTokenValidator()).realm(' ')  | 'No authentication realm set'
    }
}
