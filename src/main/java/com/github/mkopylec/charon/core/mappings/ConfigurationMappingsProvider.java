package com.github.mkopylec.charon.core.mappings;

import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.MappingProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ConfigurationMappingsProvider extends MappingsProvider {

    public ConfigurationMappingsProvider(ServerProperties server, CharonProperties charon, MappingsCorrector mappingsCorrector) {
        super(server, charon, mappingsCorrector);
    }

    @Override
    protected boolean shouldUpdateMappings(HttpServletRequest request) {
        return false;
    }

    @Override
    protected List<MappingProperties> retrieveMappings() {
        return charon.getMappings().stream()
                .map(MappingProperties::copy)
                .collect(toList());
    }
}
