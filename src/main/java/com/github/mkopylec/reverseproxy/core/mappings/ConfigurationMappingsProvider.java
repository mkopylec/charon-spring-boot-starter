package com.github.mkopylec.reverseproxy.core.mappings;

import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties;
import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties.Mapping;
import org.springframework.scheduling.TaskScheduler;

import java.util.List;

public class ConfigurationMappingsProvider extends MappingsProvider {

	protected final ReverseProxyProperties reverseProxy;

	public ConfigurationMappingsProvider(TaskScheduler scheduler, ReverseProxyProperties reverseProxy) {
		super(scheduler, reverseProxy);
		this.reverseProxy = reverseProxy;
	}

	@Override
	protected List<Mapping> retrieveMappings() {
		return reverseProxy.getMappings();
	}
}
