package com.github.mkopylec.reverseproxy.configuration;

import com.github.mkopylec.reverseproxy.core.balancer.LoadBalancer;
import com.github.mkopylec.reverseproxy.core.balancer.RandomLoadBalancer;
import com.github.mkopylec.reverseproxy.core.http.HttpProxyFilter;
import com.github.mkopylec.reverseproxy.core.http.RequestDataExtractor;
import com.github.mkopylec.reverseproxy.core.mappings.ConfigurationMappingsProvider;
import com.github.mkopylec.reverseproxy.core.mappings.MappingsProvider;
import com.github.mkopylec.reverseproxy.exceptions.ReverseProxyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public FilterRegistrationBean rpHttpProxyFilterRegistrationBean(HttpProxyFilter proxyFilter) {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean(proxyFilter);
		registrationBean.setOrder(reverseProxy.getFilterOrder());
		return registrationBean;
	}

	@Bean
	@ConditionalOnMissingBean
	public HttpProxyFilter rpHttpProxyFilter(
			RestOperations restOperations,
			RetryOperations retryOperations,
			RequestDataExtractor extractor,
			MappingsProvider mappingsProvider,
			LoadBalancer loadBalancer
	) {
		return new HttpProxyFilter(reverseProxy, restOperations, retryOperations, extractor, mappingsProvider, loadBalancer);
	}

	@Bean
	@ConditionalOnMissingBean
	public RestOperations rpRestOperations() {
		Netty4ClientHttpRequestFactory requestFactory = new Netty4ClientHttpRequestFactory();
		requestFactory.setConnectTimeout(reverseProxy.getTimeout().getConnect());
		requestFactory.setReadTimeout(reverseProxy.getTimeout().getRead());
		return new RestTemplate(requestFactory);
	}

	@Bean
	@ConditionalOnMissingBean
	public RetryOperations rpRetryOperations() {
		Map<Class<? extends Throwable>, Boolean> retryableExceptions = new HashMap<>(1);
		retryableExceptions.put(ResourceAccessException.class, true);
		SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(reverseProxy.getRetrying().getMaxAttempts(), retryableExceptions);
		RetryTemplate retryTemplate = new RetryTemplate();
		retryTemplate.setRetryPolicy(retryPolicy);
		return retryTemplate;
	}

	@Bean
	@ConditionalOnMissingBean
	public RequestDataExtractor rpRequestDataExtractor() {
		return new RequestDataExtractor();
	}

	@Bean
	@ConditionalOnMissingBean
	@Autowired(required = false)
	public MappingsProvider rpMappingsProvider(TaskScheduler scheduler) {
		return new ConfigurationMappingsProvider(scheduler, reverseProxy);
	}

	@Bean
	@ConditionalOnMissingBean
	public LoadBalancer rpLoadBalancer() {
		return new RandomLoadBalancer();
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty("reverse-proxy.mappings-update.enabled")
	public TaskScheduler rpTaskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(2);
		return scheduler;
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
		int maxAttempts = reverseProxy.getRetrying().getMaxAttempts();
		if (maxAttempts < 1) {
			throw new ReverseProxyException("Invalid max number of attempts to send request value: " + maxAttempts);
		}
		if (reverseProxy.getMappingsUpdate().isEnabled()) {
			int mappingsUpdateInterval = reverseProxy.getMappingsUpdate().getIntervalInMillis();
			if (mappingsUpdateInterval < 0) {
				throw new ReverseProxyException("Invalid mappings update interval value: " + mappingsUpdateInterval);
			}
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
