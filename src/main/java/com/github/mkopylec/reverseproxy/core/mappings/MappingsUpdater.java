package com.github.mkopylec.reverseproxy.core.mappings;

import org.springframework.scheduling.annotation.Scheduled;

public class MappingsUpdater {

	protected final MappingsProvider mappingsProvider;

	public MappingsUpdater(MappingsProvider mappingsProvider) {
		this.mappingsProvider = mappingsProvider;
	}

	@Scheduled(fixedRateString = "${reverse-proxy.mappings-update.interval-in-millis}")
	protected void renewMappings() {
		mappingsProvider.updateMappings();
	}
}
