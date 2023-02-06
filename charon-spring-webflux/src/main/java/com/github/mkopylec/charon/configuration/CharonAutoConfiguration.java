package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.forwarding.ReverseProxyFilter;
import org.slf4j.Logger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static org.slf4j.LoggerFactory.getLogger;

@AutoConfiguration
@ConditionalOnBean(CharonConfigurer.class)
class CharonAutoConfiguration {

    private static final Logger log = getLogger(CharonAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    ReverseProxyFilter reverseProxyFilter(ObjectProvider<CharonConfigurer> charonConfigurer) {
        CharonConfigurer configurer = charonConfigurer.getIfAvailable();
        if (configurer == null) {
            log.warn("No Charon configuration detected, all incoming requests will be handled locally");
            configurer = charonConfiguration();
        }
        CharonConfiguration configuration = configurer.configure();
        return new ReverseProxyFilter(configuration.getFilterOrder(), configuration.getRequestMappingConfigurations());
    }
}
