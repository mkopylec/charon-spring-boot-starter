package com.github.mkopylec.reverseproxy.core.mappings;

import org.slf4j.Logger;

import org.springframework.scheduling.annotation.Scheduled;

import static org.slf4j.LoggerFactory.getLogger;

public class MappingsUpdater {

	private static final Logger log = getLogger(MappingsUpdater.class);

	protected final MappingsProvider mappingsProvider;

	public MappingsUpdater(MappingsProvider mappingsProvider) {
		this.mappingsProvider = mappingsProvider;
	}

	@Scheduled(fixedRateString = "${reverse-proxy.mappings-update.interval-in-millis}")
	protected void renewMappings() {
		mappingsProvider.updateMappings();
		log.trace("Mappings updated to: {}", mappingsProvider.getMappings());
	}
}
