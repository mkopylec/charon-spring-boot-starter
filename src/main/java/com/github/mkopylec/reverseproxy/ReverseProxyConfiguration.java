package com.github.mkopylec.reverseproxy;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.github.mkopylec.reverseproxy.ReverseProxyProperties.Mapping;
import com.github.mkopylec.reverseproxy.core.ConfigurationMappingsProvider;
import com.github.mkopylec.reverseproxy.core.HttpProxyFilter;
import com.github.mkopylec.reverseproxy.core.LoadBalancer;
import com.github.mkopylec.reverseproxy.core.MappingsProvider;
import com.github.mkopylec.reverseproxy.core.RandomLoadBalancer;
import com.github.mkopylec.reverseproxy.core.RequestDataExtractor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.prependIfMissing;
import static org.apache.commons.lang3.StringUtils.removeEnd;

@Configuration
@EnableConfigurationProperties(ReverseProxyProperties.class)
public class ReverseProxyConfiguration {

	@Autowired
	protected ReverseProxyProperties reverseProxy;

	@Bean
	public FilterRegistrationBean httpProxyFilterRegistrationBean(HttpProxyFilter proxyFilter) {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(proxyFilter);
		registrationBean.setOrder(reverseProxy.getFilterOrder());
		registrationBean.setUrlPatterns(singletonList("/**"));
		return registrationBean;
	}

	@Bean
	@ConditionalOnMissingBean
	public HttpProxyFilter httpProxyFilter(RestTemplate restTemplate, RequestDataExtractor extractor, MappingsProvider mappingsProvider, LoadBalancer loadBalancer) {
		return new HttpProxyFilter(reverseProxy, restTemplate, extractor, mappingsProvider, loadBalancer);
	}

	@Bean
	@ConditionalOnMissingBean
	public RestTemplate restTemplate() {
		Netty4ClientHttpRequestFactory requestFactory = new Netty4ClientHttpRequestFactory();
		requestFactory.setConnectTimeout(reverseProxy.getConnectTimeout());
		requestFactory.setReadTimeout(reverseProxy.getReadTimeout());
		return new RestTemplate(requestFactory);
	}

	@Bean
	@ConditionalOnMissingBean
	public MappingsProvider mappingsProvider() {
		return new ConfigurationMappingsProvider(reverseProxy);
	}

	@Bean
	@ConditionalOnMissingBean
	public LoadBalancer loadBalancer() {
		return new RandomLoadBalancer();
	}

	@PostConstruct
	protected void checkConfiguration() {
		if (reverseProxy.getConnectTimeout() < 0) {
			throw new ReverseProxyException("Invalid connect timeout value: " + reverseProxy.getConnectTimeout());
		}
		if (reverseProxy.getReadTimeout() < 0) {
			throw new ReverseProxyException("Invalid read timeout value: " + reverseProxy.getReadTimeout());
		}
		if (isNotEmpty(reverseProxy.getMappings())) {
			reverseProxy.getMappings().forEach(this::correctMapping);
			int numberOfPaths = reverseProxy.getMappings()
					.stream()
					.map(Mapping::getPath)
					.collect(toSet())
					.size();
			if (numberOfPaths < reverseProxy.getMappings().size()) {
				throw new ReverseProxyException("Duplicated destination paths in mappings");
			}
		}
	}

	protected void correctMapping(Mapping mapping) {
		correctHosts(mapping);
		correctPath(mapping);
	}

	protected void correctHosts(Mapping mapping) {
		if (isEmpty(mapping.getHosts())) {
			throw new ReverseProxyException("No destination hosts for mapping " + mapping);
		}
		List<String> correctedHosts = new ArrayList<>(mapping.getHosts().size());
		mapping.getHosts().forEach(host -> {
			if (isBlank(host)) {
				throw new ReverseProxyException("Empty destination host for mapping " + mapping);
			}
			if (!host.matches(".+://.+")) {
				host = "http://" + host;
			}
			host = removeEnd(host, "/");
			correctedHosts.add(host);
		});
		mapping.setHosts(correctedHosts);
	}

	protected void correctPath(Mapping mapping) {
		if (isBlank(mapping.getPath())) {
			throw new ReverseProxyException("No destination path for mapping " + mapping);
		}
		mapping.setPath(removeEnd(prependIfMissing(mapping.getPath(), "/"), "/"));
	}
}
