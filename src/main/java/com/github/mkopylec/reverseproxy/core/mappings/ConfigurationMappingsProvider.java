package com.github.mkopylec.reverseproxy.core.mappings;

import java.util.List;

import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties;
import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties.Mapping;

public class ConfigurationMappingsProvider extends MappingsProvider {

	protected final ReverseProxyProperties reverseProxy;

	public ConfigurationMappingsProvider(ReverseProxyProperties reverseProxy) {
		this.reverseProxy = reverseProxy;
	}

	@Override
	protected List<Mapping> retrieveMappings() {
		return reverseProxy.getMappings();
	}
}
