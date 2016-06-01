package com.github.mkopylec.reverseproxy.core.mappings;

import java.util.List;

import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties.Mapping;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

public abstract class MappingsProvider {

	private static final Logger log = getLogger(MappingsProvider.class);

	protected List<Mapping> mappings;

	public List<Mapping> getMappings() {
		return mappings;
	}

	public void updateMappings() {
		mappings = retrieveMappings();
		log.trace("Mappings updated to: {}", mappings);
	}

	protected abstract List<Mapping> retrieveMappings();
}
