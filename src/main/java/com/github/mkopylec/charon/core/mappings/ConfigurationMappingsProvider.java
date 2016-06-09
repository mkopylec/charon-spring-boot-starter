package com.github.mkopylec.charon.core.mappings;

import java.util.List;

import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.CharonProperties.Mapping;

import org.springframework.scheduling.TaskScheduler;

public class ConfigurationMappingsProvider extends MappingsProvider {

    public ConfigurationMappingsProvider(TaskScheduler scheduler, CharonProperties charon, MappingsCorrector mappingsCorrector) {
        super(scheduler, charon, mappingsCorrector);
    }

    @Override
    protected List<Mapping> retrieveMappings() {
        return charon.getMappings();
    }
}
