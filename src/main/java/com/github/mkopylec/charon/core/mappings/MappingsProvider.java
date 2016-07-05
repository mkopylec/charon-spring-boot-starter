package com.github.mkopylec.charon.core.mappings;

import java.util.List;

import javax.annotation.PostConstruct;

import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.CharonProperties.Mapping;
import com.github.mkopylec.charon.exceptions.CharonException;
import org.slf4j.Logger;

import org.springframework.boot.autoconfigure.web.ServerProperties;

import static com.github.mkopylec.charon.utils.UriCorrector.correctUri;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class MappingsProvider {

    private static final Logger log = getLogger(MappingsProvider.class);

    protected final ServerProperties server;
    protected final CharonProperties charon;
    protected final MappingsCorrector mappingsCorrector;
    protected List<Mapping> mappings;

    public MappingsProvider(ServerProperties server, CharonProperties charon, MappingsCorrector mappingsCorrector) {
        this.server = server;
        this.charon = charon;
        this.mappingsCorrector = mappingsCorrector;
    }

    public Mapping resolveMapping(String originUri) {
        List<Mapping> resolvedMappings = mappings.stream()
                .filter(mapping -> originUri.startsWith(concatContextAndMappingPaths(mapping)))
                .collect(toList());
        if (isEmpty(resolvedMappings)) {
            return null;
        }
        if (resolvedMappings.size() == 1) {
            return resolvedMappings.get(0);
        }
        throw new CharonException("Multiple mapping paths found for HTTP request URI: " + originUri);
    }

    public void updateMappingsIfAllowed() {
        if (charon.getMappingsUpdate().isEnabled()) {
            updateMappings();
        }
    }

    @PostConstruct
    protected synchronized void updateMappings() {
        List<Mapping> newMappings = retrieveMappings();
        mappingsCorrector.correct(newMappings);
        mappings = newMappings;
        log.trace("Destination mappings updated to: {}", mappings);
    }

    protected String concatContextAndMappingPaths(Mapping mapping) {
        return correctUri(server.getContextPath()) + mapping.getPath();
    }

    protected abstract List<Mapping> retrieveMappings();
}
