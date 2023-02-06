package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.configuration.CharonConfigurer;
import com.github.mkopylec.charon.forwarding.interceptors.HttpResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.configuration.RequestMappingConfigurer.requestMapping;
import static com.github.mkopylec.charon.forwarding.TimeoutConfigurer.timeout;
import static com.github.mkopylec.charon.forwarding.WebClientConfigurer.webClient;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.REQUEST_PROTOCOL_HEADERS_REWRITER;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.REQUEST_PROXY_HEADERS_REWRITER;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RESPONSE_COOKIE_REWRITER;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RESPONSE_PROTOCOL_HEADERS_REWRITER;
import static com.github.mkopylec.charon.forwarding.interceptors.async.AsynchronousForwarderConfigurer.asynchronousForwarder;
import static com.github.mkopylec.charon.forwarding.interceptors.metrics.LatencyMeterConfigurer.latencyMeter;
import static com.github.mkopylec.charon.forwarding.interceptors.metrics.RateMeterConfigurer.rateMeter;
import static com.github.mkopylec.charon.forwarding.interceptors.resilience.CircuitBreakerConfigurer.circuitBreaker;
import static com.github.mkopylec.charon.forwarding.interceptors.resilience.RateLimiterConfigurer.rateLimiter;
import static com.github.mkopylec.charon.forwarding.interceptors.resilience.RetryerConfigurer.retryer;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RegexRequestPathRewriterConfigurer.regexRequestPathRewriter;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RemovingResponseCookiesRewriterConfigurer.removingResponseCookiesRewriter;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestHostHeaderRewriterConfigurer.requestHostHeaderRewriter;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestServerNameRewriterConfigurer.requestServerNameRewriter;
import static com.github.mkopylec.charon.forwarding.interceptors.security.BasicAuthenticatorConfigurer.basicAuthenticator;
import static com.github.mkopylec.charon.forwarding.interceptors.security.BearerAuthenticatorConfigurer.bearerAuthenticator;
import static com.github.mkopylec.charon.forwarding.interceptors.security.InMemoryTokenValidatorConfigurer.inMemoryTokenValidator;
import static com.github.mkopylec.charon.forwarding.interceptors.security.InMemoryUserValidatorConfigurer.inMemoryUserValidator;
import static com.github.mkopylec.charon.test.ExceptionThrowerConfigurer.exceptionThrower;
import static com.github.mkopylec.charon.test.LowestPortLoadBalancerConfigurer.lowestPortLoadBalancer;
import static com.github.mkopylec.charon.test.RequestBodyRewriterConfigurer.requestBodyRewriter;
import static com.github.mkopylec.charon.test.ResponseBodyRewriterConfigurer.responseBodyRewriter;
import static com.github.mkopylec.charon.test.utils.MeterRegistryProvider.meterRegistry;
import static java.time.Duration.ZERO;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static java.time.Duration.ofSeconds;
import static java.util.Collections.singletonList;

@Configuration
class ReverseProxyConfiguration {

    private static final String CHARON_CONFIGURER_BEAN = "charonConfigurer";

    @Value("${default-charon-configuration}")
    private boolean defaultConfiguration;

    @Profile("default")
    @Bean(CHARON_CONFIGURER_BEAN)
    CharonConfigurer defaultProfileCharonConfigurer() {
        if (defaultConfiguration) {
            return null;
        }
        return charonConfigurer();
    }

    @Profile("defined")
    @Bean(CHARON_CONFIGURER_BEAN)
    CharonConfigurer definedProfileCharonConfigurer() {
        return charonConfigurer()
                .set(requestServerNameRewriter().outgoingServers("localhost:8081"))
                .add(requestMapping("new mapping")
                        .pathRegex("/new/mapping"))
                .add(requestMapping("request body rewriting")
                        .pathRegex("/overwritten/request/body/rewriting.*"))
                .update("response body rewriting", configurer -> configurer.pathRegex("/updated/response/body/rewriting.*"));
    }

    private CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(requestServerNameRewriter().outgoingServers("localhost:8080", "localhost:8081"))
                .set(webClient().set(timeout().read(ofMinutes(10)).write(ofMinutes(10))))
                .add(requestMapping("default")
                        .pathRegex("/default"))
                .add(requestMapping("asynchronous forwarding")
                        .pathRegex("/asynchronous/forwarding.*")
                        .set(asynchronousForwarder()))
                .add(requestMapping("exception asynchronous forwarding")
                        .pathRegex("/exception/asynchronous/forwarding.*")
                        .set(requestServerNameRewriter().outgoingServers("http://non-existing.charonhost"))
                        .set(asynchronousForwarder()))
                .add(requestMapping("request protocol headers rewriting")
                        .pathRegex("/request/protocol/headers.*")
                        .unset(REQUEST_PROTOCOL_HEADERS_REWRITER))
                .add(requestMapping("request proxy headers rewriting")
                        .pathRegex("/request/proxy/headers")
                        .unset(REQUEST_PROXY_HEADERS_REWRITER))
                .add(requestMapping("request host header rewriting")
                        .pathRegex("/request/host/header.*")
                        .set(requestServerNameRewriter().outgoingServers("localhost:8080"))
                        .set(requestHostHeaderRewriter()))
                .add(requestMapping("regex request path rewriting encoded")
                        .pathRegex("/regex/request/path%20encoded")
                        .set(regexRequestPathRewriter()))
                .add(requestMapping("regex request path rewriting groups")
                        .pathRegex("/regex/request/path/groups")
                        .set(regexRequestPathRewriter().paths("/regex/request/(?<path>.*)", "/group/template/<path>")))
                .add(requestMapping("regex request path rewriting group in incoming")
                        .pathRegex("/regex/request/path/groups/in/incoming.*")
                        .set(regexRequestPathRewriter().paths("/regex/(?<path>.*)", "/no/group/template")))
                .add(requestMapping("regex request path rewriting group in outgoing")
                        .pathRegex("/regex/request/path/groups/in/outgoing.*")
                        .set(regexRequestPathRewriter().paths("/regex.*", "/<path>")))
                .add(requestMapping("regex request path rewriting no groups")
                        .pathRegex("/regex/request/path/no/groups.*")
                        .set(regexRequestPathRewriter().paths("/regex.*", "/no/group/template")))
                .add(requestMapping("regex request path rewriting no match")
                        .pathRegex("/regex/request/path/no/match.*")
                        .set(regexRequestPathRewriter().paths("/no/match/(?<path>.*)", "/<path>")))
                .add(requestMapping("response protocol headers rewriting")
                        .pathRegex("/response/protocol/headers.*")
                        .unset(RESPONSE_PROTOCOL_HEADERS_REWRITER)
                        .unset(REQUEST_PROTOCOL_HEADERS_REWRITER))
                .add(requestMapping("root path response cookies rewriting")
                        .pathRegex("/root/path/response/cookies.*")
                        .unset(RESPONSE_COOKIE_REWRITER))
                .add(requestMapping("removing response cookies rewriting")
                        .pathRegex("/removing/response/cookies.*")
                        .set(removingResponseCookiesRewriter()))
                .add(requestMapping("circuit breaking")
                        .pathRegex("/circuit/breaking.*")
                        .set(requestServerNameRewriter().outgoingServers("localhost:8080"))
                        .set(circuitBreaker().configuration(CircuitBreakerConfig.custom()
                                        .slidingWindowSize(1)
                                        .recordResult(result -> ((HttpResponse) result).statusCode().is5xxServerError()))
                                .meterRegistry(meterRegistry())))
                .add(requestMapping("exception circuit breaking")
                        .pathRegex("/exception/circuit/breaking.*")
                        .set(requestServerNameRewriter().outgoingServers("http://non-existing.charonhost"))
                        .set(circuitBreaker().configuration(CircuitBreakerConfig.custom()
                                        .slidingWindowSize(1))
                                .meterRegistry(meterRegistry())))
                .add(requestMapping("fallback circuit breaking")
                        .pathRegex("/fallback/circuit/breaking.*")
                        .set(requestServerNameRewriter().outgoingServers("http://non-existing.charonhost"))
                        .set(circuitBreaker().fallback(new CircuitBreakerFallback()).configuration(CircuitBreakerConfig.custom()
                                        .slidingWindowSize(1))
                                .meterRegistry(meterRegistry())))
                .add(requestMapping("retrying")
                        .pathRegex("/retrying.*")
                        .set(requestServerNameRewriter().outgoingServers("localhost:8080"))
                        .set(retryer().meterRegistry(meterRegistry())))
                .add(requestMapping("interceptors retrying")
                        .pathRegex("/interceptors/retrying.*")
                        .set(requestServerNameRewriter().outgoingServers("localhost:8080"))
                        .set(webClient().set(singletonList(new RetryAttemptsResponseHeaderSetter())))
                        .set(retryer().meterRegistry(meterRegistry())))
                .add(requestMapping("exception retrying")
                        .pathRegex("/exception/retrying.*")
                        .set(requestServerNameRewriter().outgoingServers("http://non-existing.charonhost"))
                        .set(retryer().meterRegistry(meterRegistry())))
                .add(requestMapping("rate limiting")
                        .pathRegex("/rate/limiting.*")
                        .set(rateLimiter().configuration(RateLimiterConfig.custom()
                                        .timeoutDuration(ZERO)
                                        .limitRefreshPeriod(ofSeconds(1))
                                        .limitForPeriod(1))
                                .meterRegistry(meterRegistry())))
                .add(requestMapping("latency metering")
                        .pathRegex("/latency/metering.*")
                        .set(latencyMeter().meterRegistry(meterRegistry())))
                .add(requestMapping("rate metering")
                        .pathRegex("/rate/metering.*")
                        .set(rateMeter().meterRegistry(meterRegistry())))
                .add(requestMapping("exception rate metering")
                        .pathRegex("/exception/rate/metering.*")
                        .set(exceptionThrower())
                        .set(rateMeter().meterRegistry(meterRegistry())))
                .add(requestMapping("multiple mappings found 1")
                        .pathRegex("/multiple/mappings/found.*"))
                .add(requestMapping("multiple mappings found 2")
                        .pathRegex("/multiple/mappings/found.*"))
                .add(requestMapping("request body rewriting")
                        .pathRegex("/request/body/rewriting.*")
                        .set(requestBodyRewriter()))
                .add(requestMapping("response body rewriting")
                        .pathRegex("/response/body/rewriting.*")
                        .set(responseBodyRewriter()))
                .add(requestMapping("basic authentication")
                        .pathRegex("/basic/authentication.*")
                        .set(basicAuthenticator().userValidator(inMemoryUserValidator()
                                .validUser("user", "password"))))
                .add(requestMapping("bearer authentication")
                        .pathRegex("/bearer/authentication.*")
                        .set(bearerAuthenticator().tokenValidator(inMemoryTokenValidator()
                                .validTokens("token"))))
                .add(requestMapping("custom load balancer")
                        .pathRegex("/custom/load/balancer.*")
                        .set(requestServerNameRewriter().outgoingServers("localhost:8080", "localhost:8081", "localhost:8082").loadBalancer(lowestPortLoadBalancer())))
                .add(requestMapping("timeout")
                        .pathRegex("/timeout.*")
                        .set(webClient().set(timeout().read(ofMillis(500)))))
                .add(requestMapping("exchange strategies")
                        .pathRegex("/exchange/strategies.*")
                        .set(webClient().set(new DataBufferSizeSetter())));
    }
}
