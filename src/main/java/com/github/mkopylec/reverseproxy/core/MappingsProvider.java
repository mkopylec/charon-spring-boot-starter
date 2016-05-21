package com.github.mkopylec.reverseproxy.core;

import java.util.List;

import com.github.mkopylec.reverseproxy.ReverseProxyProperties.Mapping;

public interface MappingsProvider {

	List<Mapping> getMappings();
}
