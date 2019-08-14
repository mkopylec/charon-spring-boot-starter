package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.forwarding.ReverseProxyFilter;
import org.slf4j.Logger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.github.mkopylec.charon.configuration.CharonConfigurer.charonConfiguration;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.Assert.isTrue;

@Configuration
@ConditionalOnBean(CharonConfigurer.class)
class CharonAutoConfiguration {

    private static final Logger log = getLogger(CharonAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    ReverseProxyFilter reverseProxyFilter(ObjectProvider<CharonConfigurer> charonConfigurer, ObjectProvider<DynamicCharonConfigurer> dynamicCharonConfigurer) {
        CharonConfigurer configurer = charonConfigurer.getIfAvailable();
        DynamicCharonConfigurer dynamicConfigurer = dynamicCharonConfigurer.getIfAvailable();
        isTrue(!(configurer != null && dynamicConfigurer != null), "Static and dynamic Charon configuration detected, only one exist");
        if (configurer == null && dynamicConfigurer == null) {
            log.warn("No Charon configuration detected, all incoming requests will be handled locally");
            CharonConfiguration configuration = charonConfiguration().configure();
            return new ReverseProxyFilter(configuration.getFilterOrder(), new StaticRequestMappingProvider(configuration.getRequestMappingConfigurations()));
        }
        if (configurer != null) {
            CharonConfiguration configuration = configurer.configure();
            return new ReverseProxyFilter(configuration.getFilterOrder(), new StaticRequestMappingProvider(configuration.getRequestMappingConfigurations()));
        }
        DynamicCharonConfiguration configuration = dynamicConfigurer.configure();
        return new ReverseProxyFilter(configuration.getUpdate().get().getFilterOrder(), new DynamicRequestMappingProvider(configuration.getUpdateRate(), configuration.getUpdate()));
    }
}
