package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.configuration.CharonConfigurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.configuration.RequestMappingConfigurer.requestMapping;
import static com.github.mkopylec.charon.forwarding.RestTemplateConfigurer.restTemplate;
import static com.github.mkopylec.charon.forwarding.TimeoutConfigurer.timeout;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.REQUEST_PROTOCOL_HEADERS_REWRITER;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.REQUEST_PROXY_HEADERS_REWRITER;
import static com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType.RESPONSE_PROTOCOL_HEADERS_REWRITER;
import static com.github.mkopylec.charon.forwarding.interceptors.async.AsynchronousForwardingHandlerConfigurer.asynchronousForwardingHandler;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RegexRequestPathRewriterConfigurer.regexRequestPathRewriter;
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
                .add(requestMapping("asynchronous forwarding")
                        .pathRegex("/asynchronous/forwarding.*")
                        .set(asynchronousForwardingHandler()))
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
                        .unset(REQUEST_PROTOCOL_HEADERS_REWRITER));
    }
}
