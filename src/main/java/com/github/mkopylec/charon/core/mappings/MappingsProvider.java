package com.github.mkopylec.charon.core.mappings;

import java.util.List;

import javax.annotation.PostConstruct;

import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.CharonProperties.Mapping;
import org.slf4j.Logger;

import org.springframework.scheduling.TaskScheduler;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class MappingsProvider {

    private static final Logger log = getLogger(MappingsProvider.class);

    protected final TaskScheduler scheduler;
    protected final CharonProperties charon;
    protected final MappingsCorrector mappingsCorrector;
    protected List<Mapping> mappings;

    public MappingsProvider(TaskScheduler scheduler, CharonProperties charon, MappingsCorrector mappingsCorrector) {
        this.scheduler = scheduler;
        this.charon = charon;
        this.mappingsCorrector = mappingsCorrector;
    }

    public List<Mapping> getMappings() {
        return mappings;
    }

    public synchronized void updateMappings() {
        mappings = retrieveMappings();
        mappingsCorrector.correct(mappings);
        log.trace("Destination mappings updated to: {}", mappings);
    }

    @PostConstruct
    protected void scheduleMappingsUpdate() {
        updateMappings();
        if (scheduler != null) {
            int intervalInMillis = charon.getMappingsUpdate().getIntervalInMillis();
            if (intervalInMillis > 0) {
                scheduler.scheduleAtFixedRate(this::updateMappings, intervalInMillis);
            }
        }
    }

    protected abstract List<Mapping> retrieveMappings();
}
