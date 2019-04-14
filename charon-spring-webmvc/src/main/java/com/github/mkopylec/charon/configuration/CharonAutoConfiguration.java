package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.forwarding.ReverseProxyFilter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(CharonConfigurer.class)
class CharonAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    ReverseProxyFilter reverseProxyFilter(CharonConfigurer configurer) {
        CharonConfiguration configuration = configurer.configure();
        return new ReverseProxyFilter(configuration.getFilterOrder(), configuration.getRequestMappingConfigurations());
    }
}
