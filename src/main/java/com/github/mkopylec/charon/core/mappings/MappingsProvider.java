package com.github.mkopylec.charon.core.mappings;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import com.github.mkopylec.charon.configuration.CharonProperties;
import com.github.mkopylec.charon.configuration.MappingProperties;
import com.github.mkopylec.charon.core.http.HttpClientProvider;
import org.slf4j.Logger;

import org.springframework.boot.autoconfigure.web.ServerProperties;

import static com.github.mkopylec.charon.core.utils.UriCorrector.correctUri;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.slf4j.LoggerFactory.getLogger;

public abstract class MappingsProvider {

    private static final Logger log = getLogger(MappingsProvider.class);

    protected final ServerProperties server;
    protected final CharonProperties charon;
    protected final MappingsCorrector mappingsCorrector;
    protected final HttpClientProvider httpClientProvider;
    protected List<MappingProperties> mappings;

    public MappingsProvider(
            ServerProperties server,
            CharonProperties charon,
            MappingsCorrector mappingsCorrector,
            HttpClientProvider httpClientProvider
    ) {
        this.server = server;
        this.charon = charon;
        this.mappingsCorrector = mappingsCorrector;
        this.httpClientProvider = httpClientProvider;
    }

    public MappingProperties resolveMapping(String originUri, HttpServletRequest request) {
        if (shouldUpdateMappings(request)) {
            updateMappings();
        }
        List<MappingProperties> resolvedMappings = mappings.stream()
                .filter(mapping -> originUri.startsWith(concatContextAndMappingPaths(mapping)))
                .collect(toList());
        if (isEmpty(resolvedMappings)) {
            return null;
        }
        return resolvedMappings.get(0);
    }

    @PostConstruct
    protected synchronized void updateMappings() {
        List<MappingProperties> newMappings = retrieveMappings();
        mappingsCorrector.correct(newMappings);
        mappings = newMappings;
        httpClientProvider.updateHttpClients(mappings);
        log.info("Destination mappings updated to: {}", mappings);
    }

    protected String concatContextAndMappingPaths(MappingProperties mapping) {
        return correctUri(server.getServlet().getContextPath()) + mapping.getPath();
    }

    protected abstract boolean shouldUpdateMappings(HttpServletRequest request);

    protected abstract List<MappingProperties> retrieveMappings();
}
