package com.github.mkopylec.charon.core.mappings;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.MappingProperties;
import com.github.mkopylec.charon.core.http.HttpClientProvider;

import org.springframework.boot.autoconfigure.web.ServerProperties;

import static java.util.stream.Collectors.toList;

public class ConfigurationMappingsProvider extends MappingsProvider {

    public ConfigurationMappingsProvider(
            ServerProperties server,
            CharonProperties charon,
            MappingsCorrector mappingsCorrector,
            HttpClientProvider httpClientProvider
    ) {
        super(server, charon, mappingsCorrector, httpClientProvider);
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
