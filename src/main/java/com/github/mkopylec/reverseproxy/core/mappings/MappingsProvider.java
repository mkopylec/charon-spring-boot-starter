package com.github.mkopylec.reverseproxy.core.mappings;

import java.util.List;

import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties.Mapping;

public abstract class MappingsProvider {

	protected List<Mapping> mappings;

	public List<Mapping> getMappings() {
		return mappings;
	}

	public void updateMappings() {
		mappings = retrieveMappings();
	}

	protected abstract List<Mapping> retrieveMappings();
}
