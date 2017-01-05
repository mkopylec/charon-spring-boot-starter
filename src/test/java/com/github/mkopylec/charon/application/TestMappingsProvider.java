package com.github.mkopylec.charon.application;

import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.core.http.HttpClientProvider;
import com.github.mkopylec.charon.core.mappings.ConfigurationMappingsProvider;
import com.github.mkopylec.charon.core.mappings.MappingsCorrector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@ConditionalOnProperty("test.mappings-provider-enabled")
public class TestMappingsProvider extends ConfigurationMappingsProvider {

    @Autowired
    public TestMappingsProvider(ServerProperties server, CharonProperties charon, MappingsCorrector mappingsCorrector, HttpClientProvider httpClientProvider) {
        super(server, charon, mappingsCorrector, httpClientProvider);
    }

    @Override
    protected boolean shouldUpdateMappings(HttpServletRequest request) {
        return true;
    }
}
