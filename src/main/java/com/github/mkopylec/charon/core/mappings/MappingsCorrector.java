package com.github.mkopylec.charon.core.mappings;

import com.github.mkopylec.charon.configuration.CharonProperties.Mapping;
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

    public void correct(List<Mapping> mappings) {
        if (isNotEmpty(mappings)) {
            mappings.forEach(this::correctMapping);
            int numberOfPaths = mappings.stream()
                    .map(Mapping::getPath)
                    .collect(toSet())
                    .size();
            if (numberOfPaths < mappings.size()) {
                throw new CharonException("Duplicated destination paths in mappings");
            }
            mappings.sort((o1, o2) -> o2.getPath().compareTo(o1.getPath()));
        }
    }

    protected void correctMapping(Mapping mapping) {
        correctName(mapping);
        correctDestinations(mapping);
        correctPath(mapping);
    }

    protected void correctName(Mapping mapping) {
        if (isBlank(mapping.getName())) {
            throw new CharonException("Empty name for mapping " + mapping);
        }
    }

    protected void correctDestinations(Mapping mapping) {
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

    protected void correctPath(Mapping mapping) {
        if (isBlank(mapping.getPath())) {
            throw new CharonException("No destination path for mapping " + mapping);
        }
        String path = correctUri(mapping.getPath());
        mapping.setPath(path);
    }
}
