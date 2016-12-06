package com.github.mkopylec.charon.core.mappings;

import com.github.mkopylec.charon.configuration.MappingProperties;
import com.github.mkopylec.charon.exceptions.CharonException;

import java.util.ArrayList;
import java.util.List;

import static com.github.mkopylec.charon.core.utils.UriCorrector.correctUri;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class MappingsCorrector {

    public void correct(List<MappingProperties> mappings) {
        if (isNotEmpty(mappings)) {
            mappings.forEach(this::correctMapping);
            int numberOfPaths = mappings.stream()
                    .map(MappingProperties::getPath)
                    .collect(toSet())
                    .size();
            if (numberOfPaths < mappings.size()) {
                throw new CharonException("Duplicated destination paths in mappings");
            }
            mappings.sort((mapping1, mapping2) -> mapping2.getPath().compareTo(mapping1.getPath()));
        }
    }

    protected void correctMapping(MappingProperties mapping) {
        correctName(mapping);
        correctDestinations(mapping);
        correctPath(mapping);
        correctTimeout(mapping);
    }

    protected void correctName(MappingProperties mapping) {
        if (isBlank(mapping.getName())) {
            throw new CharonException("Empty name for mapping " + mapping);
        }
    }

    protected void correctDestinations(MappingProperties mapping) {
        if (isEmpty(mapping.getDestinations())) {
            throw new CharonException("No destination hosts for mapping " + mapping);
        }
        List<String> correctedHosts = new ArrayList<>(mapping.getDestinations().size());
        mapping.getDestinations().forEach(destination -> {
            if (isBlank(destination)) {
                throw new CharonException("Empty destination for mapping " + mapping);
            }
            if (!destination.matches(".+://.+")) {
                destination = "http://" + destination;
            }
            destination = removeEnd(destination, "/");
            correctedHosts.add(destination);
        });
        mapping.setDestinations(correctedHosts);
    }

    protected void correctPath(MappingProperties mapping) {
        if (isBlank(mapping.getPath())) {
            throw new CharonException("No destination path for mapping " + mapping);
        }
        String path = correctUri(mapping.getPath());
        mapping.setPath(path);
    }

    protected void correctTimeout(MappingProperties mapping) {
        int connectTimeout = mapping.getTimeout().getConnect();
        if (connectTimeout < 0) {
            throw new CharonException("Invalid connect timeout value: " + connectTimeout);
        }
        int readTimeout = mapping.getTimeout().getRead();
        if (readTimeout < 0) {
            throw new CharonException("Invalid read timeout value: " + readTimeout);
        }
    }
    //TODO correct hystrix fallback repsonse
}
