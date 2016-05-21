package com.github.mkopylec.reverseproxy.core;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.mkopylec.reverseproxy.ReverseProxyException;
import com.github.mkopylec.reverseproxy.ReverseProxyProperties;
import com.github.mkopylec.reverseproxy.ReverseProxyProperties.Mapping;
import org.slf4j.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.ResponseEntity.status;

public class HttpProxyFilter extends OncePerRequestFilter {

	private static final Logger log = getLogger(HttpProxyFilter.class);

	protected final ReverseProxyProperties reverseProxy;
	protected final RestTemplate restTemplate;
	protected final RequestDataExtractor extractor;
	protected final MappingsProvider mappingsProvider;
	protected final LoadBalancer loadBalancer;

	public HttpProxyFilter(ReverseProxyProperties reverseProxy, RestTemplate restTemplate, RequestDataExtractor extractor, MappingsProvider mappingsProvider, LoadBalancer loadBalancer) {
		this.reverseProxy = reverseProxy;
		this.restTemplate = restTemplate;
		this.extractor = extractor;
		this.mappingsProvider = mappingsProvider;
		this.loadBalancer = loadBalancer;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String uri = extractor.extractUri(request);
		URI url = resolveDestinationUrl(uri);
		if (url == null) {
			filterChain.doFilter(request, response);
			log.debug("{} {} {}", request.getMethod(), uri, response.getStatus());
			return;
		}
		String body = extractor.extractBody(request);
		HttpHeaders headers = extractor.extractHttpHeaders(request);
		HttpMethod method = extractor.extractHttpMethod(request);
		RequestEntity<String> requestEntity = new RequestEntity<>(body, headers, method, url);
		ResponseEntity<byte[]> responseEntity = sendRequest(requestEntity);
		processResponse(response, responseEntity);
		log.debug("{} {} -> {} {}", request.getMethod(), uri, url, responseEntity.getStatusCode().value());
	}

	protected URI resolveDestinationUrl(String uri) {
		List<URI> urls = mappingsProvider.getMappings().stream()
				.filter(mapping -> uri.startsWith(mapping.getPath()))
				.map(mapping -> createDestinationUrl(uri, mapping))
				.collect(toList());
		if (isEmpty(urls)) {
			return null;
		}
		if (urls.size() == 1) {
			return urls.get(0);
		}
		throw new ReverseProxyException("Multiple mapping paths found for HTTP request URI: " + uri);
	}

	protected URI createDestinationUrl(String uri, Mapping mapping) {
		if (mapping.isStripPath()) {
			uri = removeStart(uri, mapping.getPath());
		}
		String host = loadBalancer.chooseHost(mapping.getHosts());
		try {
			return new URI(host + uri);
		} catch (URISyntaxException e) {
			throw new ReverseProxyException("Error creating destination URL from HTTP request URI: " + uri + " for mapping " + mapping, e);
		}
	}

	protected ResponseEntity<byte[]> sendRequest(RequestEntity<String> requestEntity) {
		ResponseEntity<byte[]> responseEntity;
		try {
			responseEntity = restTemplate.exchange(requestEntity, byte[].class);
		} catch (HttpStatusCodeException e) {
			responseEntity = status(e.getStatusCode())
					.headers(e.getResponseHeaders())
					.body(e.getResponseBodyAsByteArray());
		}
		return responseEntity;
	}

	protected void processResponse(HttpServletResponse response, ResponseEntity<byte[]> responseEntity) throws IOException {
		response.setStatus(responseEntity.getStatusCode().value());
		responseEntity.getHeaders().forEach((name, values) ->
				values.forEach(value -> response.addHeader(name, value))
		);
		response.getOutputStream().write(responseEntity.getBody());
	}
}
