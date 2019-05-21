package com.github.mkopylec.charon.test;

import com.github.mkopylec.charon.configuration.CharonConfigurer;

import org.springframework.context.annotation.Configuration;

import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;

@Configuration
class ReverseProxyConfiguration {

    CharonConfigurer charonConfigurer() {
        return charonConfiguration()
                ;
    }
}
