package com.github.mkopylec.reverseproxy.core.mappings;

import java.util.ArrayList;
import java.util.List;

import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties;
import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties.Mapping;
import com.github.mkopylec.reverseproxy.exceptions.ReverseProxyException;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.prependIfMissing;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class MappingsCorrector {

	public void correct(List<Mapping> mappings) {
		if (isNotEmpty(mappings)) {
			mappings.forEach(this::correctMapping);
			int numberOfPaths = mappings.stream()
					.map(ReverseProxyProperties.Mapping::getPath)
					.collect(toSet())
					.size();
			if (numberOfPaths < mappings.size()) {
				throw new ReverseProxyException("Duplicated destination paths in mappings");
			}
		}
	}

	protected void correctMapping(Mapping mapping) {
		correctDestinations(mapping);
		correctPath(mapping);
	}

	protected void correctDestinations(Mapping mapping) {
		if (isEmpty(mapping.getDestinations())) {
			throw new ReverseProxyException("No destination hosts for mapping " + mapping);
		}
		List<String> correctedHosts = new ArrayList<>(mapping.getDestinations().size());
		mapping.getDestinations().forEach(destination -> {
			if (isBlank(destination)) {
				throw new ReverseProxyException("Empty destination for mapping " + mapping);
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
			throw new ReverseProxyException("No destination path for mapping " + mapping);
		}
		mapping.setPath(removeEnd(prependIfMissing(mapping.getPath(), "/"), "/"));
	}
}
