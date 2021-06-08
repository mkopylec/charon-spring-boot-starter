package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.net.URI;

import static org.springframework.http.HttpHeaders.readOnlyHttpHeaders;

public class BodilessHttpRequest {

    private HttpMethod method;
    private URI url;
    private String serverName;
    private HttpHeaders headers;

    BodilessHttpRequest(HttpMethod method, URI url, HttpHeaders headers) {
        this.method = method;
        this.url = url;
        serverName = url.getScheme() + "://" + url.getAuthority();
        this.headers = readOnlyHttpHeaders(headers);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public URI getUrl() {
        return url;
    }

    public String getServerName() {
        return serverName;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }
}
