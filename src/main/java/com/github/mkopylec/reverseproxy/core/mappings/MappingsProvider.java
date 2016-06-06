package com.github.mkopylec.reverseproxy.core.mappings;

import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties;
import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties.Mapping;
import org.slf4j.Logger;
import org.springframework.scheduling.TaskScheduler;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class MappingsProvider {

    private static final Logger log = getLogger(MappingsProvider.class);

    protected final TaskScheduler scheduler;
    protected final ReverseProxyProperties reverseProxy;
    protected final MappingsCorrector mappingsCorrector;
    protected List<Mapping> mappings;

    public MappingsProvider(TaskScheduler scheduler, ReverseProxyProperties reverseProxy, MappingsCorrector mappingsCorrector) {
        this.scheduler = scheduler;
        this.reverseProxy = reverseProxy;
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
            int intervalInMillis = reverseProxy.getMappingsUpdate().getIntervalInMillis();
            if (intervalInMillis > 0) {
                scheduler.scheduleAtFixedRate(this::updateMappings, intervalInMillis);
            }
        }
    }

    protected abstract List<Mapping> retrieveMappings();
}
