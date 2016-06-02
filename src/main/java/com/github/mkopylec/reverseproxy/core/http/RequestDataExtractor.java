package com.github.mkopylec.reverseproxy.core.http;

import com.github.mkopylec.reverseproxy.exceptions.ReverseProxyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import static java.util.Collections.list;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpMethod.resolve;

public class RequestDataExtractor {

	public String extractBody(HttpServletRequest request) {
		StringBuilder body = new StringBuilder();
		try {
			request.getReader().lines().forEach(body::append);
		} catch (IOException e) {
			throw new ReverseProxyException("Error extracting body of HTTP request with URI: " + extractUri(request), e);
		}
		return body.toString();
	}

	public HttpHeaders extractHttpHeaders(HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			List<String> value = list(request.getHeaders(name));
			headers.put(name, value);
		}
		return headers;
	}

	public HttpMethod extractHttpMethod(HttpServletRequest request) {
		return resolve(request.getMethod());
	}

	public String extractUri(HttpServletRequest request) {
		return request.getRequestURI() + getQuery(request);
	}

	protected String getQuery(HttpServletRequest request) {
		return request.getQueryString() == null ? EMPTY : "?" + request.getQueryString();
	}
}
