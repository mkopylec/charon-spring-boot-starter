package com.github.mkopylec.charon.core.mappings;

import java.util.List;

import javax.annotation.PostConstruct;

import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.CharonProperties.Mapping;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class MappingsProvider {

    private static final Logger log = getLogger(MappingsProvider.class);

    protected final CharonProperties charon;
    protected final MappingsCorrector mappingsCorrector;
    protected List<Mapping> mappings;

    public MappingsProvider(CharonProperties charon, MappingsCorrector mappingsCorrector) {
        this.charon = charon;
        this.mappingsCorrector = mappingsCorrector;
    }

    public List<Mapping> getMappings() {
        return mappings;
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

    protected abstract List<Mapping> retrieveMappings();
}
