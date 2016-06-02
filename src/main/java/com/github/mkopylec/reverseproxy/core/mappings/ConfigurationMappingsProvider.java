package com.github.mkopylec.reverseproxy.core.mappings;

import java.util.List;

import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties;
import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties.Mapping;

import org.springframework.scheduling.TaskScheduler;

public class ConfigurationMappingsProvider extends MappingsProvider {

	protected final ReverseProxyProperties reverseProxy;

	public ConfigurationMappingsProvider(TaskScheduler scheduler, ReverseProxyProperties reverseProxy, MappingsCorrector mappingsCorrector) {
		super(scheduler, reverseProxy, mappingsCorrector);
		this.reverseProxy = reverseProxy;
	}

	@Override
	protected List<Mapping> retrieveMappings() {
		return reverseProxy.getMappings();
	}
}
