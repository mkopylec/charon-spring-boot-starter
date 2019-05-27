package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.configuration.CharonConfigurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static com.github.mkopylec.charon.configuration.RequestMappingConfigurer.requestMapping;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestServerNameRewriterConfigurer.requestServerNameRewriter;

@Configuration
class ReverseProxyConfiguration {

    @Bean
    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                .set(requestServerNameRewriter()
                        .outgoingServers("localhost:8080"))
                .add(requestMapping("after server name request headers rewriter")
                        .pathRegex("/path/1.*"));
    }
}
