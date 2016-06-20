package com.github.mkopylec.charon.core.mappings;

import java.util.List;

import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.CharonProperties.Mapping;

import static java.util.stream.Collectors.toList;

public class ConfigurationMappingsProvider extends MappingsProvider {

    public ConfigurationMappingsProvider(CharonProperties charon, MappingsCorrector mappingsCorrector) {
        super(charon, mappingsCorrector);
    }

    @Override
    protected List<Mapping> retrieveMappings() {
        return charon.getMappings().stream()
                .map(Mapping::copy)
                .collect(toList());
    }
}
