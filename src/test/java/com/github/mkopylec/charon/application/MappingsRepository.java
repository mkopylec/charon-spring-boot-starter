package com.github.mkopylec.charon.application;

import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.MappingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Collections.emptyList;

@Component
@EnableConfigurationProperties(CharonProperties.class)
public class MappingsRepository {

    private final List<MappingProperties> mappings = new CopyOnWriteArrayList<>();

    public MappingsRepository(CharonProperties charon) {
        mappings.addAll(charon.getMappings());
    }

    public List<MappingProperties> getMappings() {
        return new ArrayList<>(mappings);
    }

    public void addMapping(String name, String path, int connectTimeout, int readTimeout) {
        addMapping(name, path, connectTimeout, readTimeout, emptyList());
    }

    public void addMapping(String name, String path, int connectTimeout, int readTimeout, List<String> destinations) {
        MappingProperties mapping = new MappingProperties();
        mapping.setName(name);
        mapping.setPath(path);
        mapping.getTimeout().setConnect(connectTimeout);
        mapping.getTimeout().setRead(readTimeout);
        mapping.setDestinations(destinations);
        mappings.add(mapping);
    }
}
