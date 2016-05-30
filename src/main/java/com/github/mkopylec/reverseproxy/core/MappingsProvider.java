package com.github.mkopylec.reverseproxy.core;

import java.util.List;

import com.github.mkopylec.reverseproxy.configuration.ReverseProxyProperties.Mapping;

public interface MappingsProvider {

	List<Mapping> getMappings();
}
