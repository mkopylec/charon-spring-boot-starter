package com.github.mkopylec.reverseproxy.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.github.mkopylec.reverseproxy.core.ConfigurationMappingsProvider;
import com.github.mkopylec.reverseproxy.core.HttpProxyFilter;
import com.github.mkopylec.reverseproxy.core.LoadBalancer;
import com.github.mkopylec.reverseproxy.core.MappingsProvider;
import com.github.mkopylec.reverseproxy.core.RandomLoadBalancer;
import com.github.mkopylec.reverseproxy.core.RequestDataExtractor;
import com.github.mkopylec.reverseproxy.exceptions.ReverseProxyException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

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
		requestFactory.setConnectTimeout(reverseProxy.getTimeout().getConnect());
		requestFactory.setReadTimeout(reverseProxy.getTimeout().getRead());
		return new RestTemplate(requestFactory);
	}

	@Bean
	@ConditionalOnMissingBean
	public RequestDataExtractor requestDataExtractor() {
		return new RequestDataExtractor();
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
		int connectTimeout = reverseProxy.getTimeout().getConnect();
		int readTimeout = reverseProxy.getTimeout().getRead();
		List<ReverseProxyProperties.Mapping> mappings = reverseProxy.getMappings();
		if (connectTimeout < 0) {
			throw new ReverseProxyException("Invalid connect timeout value: " + connectTimeout);
		}
		if (readTimeout < 0) {
			throw new ReverseProxyException("Invalid read timeout value: " + readTimeout);
		}
		if (isNotEmpty(mappings)) {
			mappings.forEach(this::correctMapping);
			int numberOfPaths = mappings.stream()
					.map(ReverseProxyProperties.Mapping::getPath)
					.collect(toSet())
					.size();
			if (numberOfPaths < mappings.size()) {
				throw new ReverseProxyException("Duplicated destination paths in mappings");
			}
		}
	}

	protected void correctMapping(ReverseProxyProperties.Mapping mapping) {
		correctDestinations(mapping);
		correctPath(mapping);
	}

	protected void correctDestinations(ReverseProxyProperties.Mapping mapping) {
		if (isEmpty(mapping.getDestinations())) {
			throw new ReverseProxyException("No destination hosts for mapping " + mapping);
		}
		List<String> correctedHosts = new ArrayList<>(mapping.getDestinations().size());
		mapping.getDestinations().forEach(destination -> {
			if (isBlank(destination)) {
				throw new ReverseProxyException("Empty destination for mapping " + mapping);
			}
			if (!destination.matches(".+://.+")) {
				destination = "http://" + destination;
			}
			destination = removeEnd(destination, "/");
			correctedHosts.add(destination);
		});
		mapping.setDestinations(correctedHosts);
	}

	protected void correctPath(ReverseProxyProperties.Mapping mapping) {
		if (isBlank(mapping.getPath())) {
			throw new ReverseProxyException("No destination path for mapping " + mapping);
		}
		mapping.setPath(removeEnd(prependIfMissing(mapping.getPath(), "/"), "/"));
	}
}
