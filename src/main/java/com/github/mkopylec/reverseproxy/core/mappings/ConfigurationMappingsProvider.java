package com.github.mkopylec.reverseproxy.core.mappings;

import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties;
import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties.Mapping;
import org.springframework.scheduling.TaskScheduler;

import java.util.List;

public class ConfigurationMappingsProvider extends MappingsProvider {

    public ConfigurationMappingsProvider(TaskScheduler scheduler, ReverseProxyProperties reverseProxy, MappingsCorrector mappingsCorrector) {
        super(scheduler, reverseProxy, mappingsCorrector);
    }

    @Override
    protected List<Mapping> retrieveMappings() {
        return reverseProxy.getMappings();
    }
}
