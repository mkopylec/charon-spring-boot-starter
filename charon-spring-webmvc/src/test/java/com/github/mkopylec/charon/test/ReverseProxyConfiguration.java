package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.configuration.CharonConfigurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.configuration.RequestMappingConfigurer.requestMapping;
import static com.github.mkopylec.charon.forwarding.RestTemplateConfigurer.restTemplate;
import static com.github.mkopylec.charon.forwarding.TimeoutConfigurer.timeout;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.REQUEST_HEADERS_REWRITER;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestHostHeaderRewriterConfigurer.requestHostHeaderRewriter;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestServerNameRewriterConfigurer.requestServerNameRewriter;
import static java.time.Duration.ofMinutes;

@Configuration
class ReverseProxyConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(requestServerNameRewriter().outgoingServers("localhost:8080", "localhost:8081"))
                .set(restTemplate().set(timeout().read(ofMinutes(10)).write(ofMinutes(10))))
                .add(requestMapping("default")
                        .pathRegex("/default"))
                .add(requestMapping("request headers rewriting")
                        .pathRegex("/request/headers")
                        .unset(REQUEST_HEADERS_REWRITER))
                .add(requestMapping("request host header rewriting")
                        .pathRegex("/request/host/header")
                        .set(requestServerNameRewriter().outgoingServers("localhost:8080"))
                        .set(requestHostHeaderRewriter()));
    }
}
