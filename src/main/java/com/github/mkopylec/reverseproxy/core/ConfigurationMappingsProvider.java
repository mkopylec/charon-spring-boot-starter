package com.github.mkopylec.reverseproxy.core;

import java.util.List;

import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties;

public class ConfigurationMappingsProvider implements MappingsProvider {

	protected final ReverseProxyProperties reverseProxy;

	public ConfigurationMappingsProvider(ReverseProxyProperties reverseProxy) {
		this.reverseProxy = reverseProxy;
	}

	@Override
	public List<ReverseProxyProperties.Mapping> getMappings() {
		return reverseProxy.getMappings();
	}
}
