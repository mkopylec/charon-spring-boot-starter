package com.github.mkopylec.charon.forwarding;

import java.io.IOException;
import java.net.URI;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import static java.net.URI.create;
import static java.util.Collections.list;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpMethod.resolve;

class HttpRequestMapper {

    HttpRequest map(HttpServletRequest request) throws IOException {
        URI uri = extractUri(request);
        HttpMethod method = extractMethod(request);
        HttpHeaders headers = extractHeaders(request);
        byte[] body = extractBody(request);
        return new HttpRequest(uri, method, headers, body);
    }

    RequestEntity<byte[]> map(HttpRequest request) {
        return new RequestEntity<>(request.getBody(), request.getHeaders(), request.getMethod(), request.getUri());
    }

    private URI extractUri(HttpServletRequest request) {
        String query = request.getQueryString() == null ? EMPTY : "?" + request.getQueryString();
        return create(request.getRequestURL().append(query).toString());
    }

    private HttpMethod extractMethod(HttpServletRequest request) {
        return resolve(request.getMethod());
    }

    private HttpHeaders extractHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            List<String> value = list(request.getHeaders(name));
            headers.put(name, value);
        }
        return headers;
    }

    private byte[] extractBody(HttpServletRequest request) throws IOException {
        return toByteArray(request.getInputStream());
    }
}
