package com.github.mkopylec.charon.forwarding;

import java.io.IOException;
import java.net.URI;
import java.util.Enumeration;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import static java.net.URI.create;
import static java.util.Collections.list;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpMethod.valueOf;

class HttpRequestMapper {

    RequestEntity<byte[]> map(HttpServletRequest request) throws IOException {
        byte[] body = extractBody(request);
        HttpHeaders headers = extractHeaders(request);
        HttpMethod method = extractMethod(request);
        URI uri = extractUri(request);
        return new RequestEntity<>(body, headers, method, uri);
    }

    private URI extractUri(HttpServletRequest request) {
        String query = request.getQueryString() == null ? EMPTY : "?" + request.getQueryString();
        return create(request.getRequestURL().append(query).toString());
    }

    private HttpMethod extractMethod(HttpServletRequest request) {
        return valueOf(request.getMethod());
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
