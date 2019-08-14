package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.configuration.CharonConfiguration;
import com.github.mkopylec.charon.configuration.RequestMappingConfiguration;
import com.github.mkopylec.charon.forwarding.RequestMappingProvider;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

class DynamicRequestMappingProvider implements RequestMappingProvider {

    private List<RequestMappingConfiguration> requestMappingConfigurations;

    DynamicRequestMappingProvider(Duration updateRate, Supplier<CharonConfiguration> configuration) {
        new ThreadPoolTaskScheduler()
                .scheduleAtFixedRate(() -> requestMappingConfigurations = configuration.get().getRequestMappingConfigurations(), updateRate);
    }

    @Override
    public List<RequestMappingConfiguration> getRequestMappingConfigurations() {
        return requestMappingConfigurations;
    }
}
