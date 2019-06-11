package com.github.mkopylec.charon.test;

import java.io.IOException;
import java.net.URI;

import com.github.mkopylec.charon.forwarding.interceptors.HttpRequest;

import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import static org.springframework.web.util.UriComponentsBuilder.fromUri;

class RequestPathAppender implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(org.springframework.http.HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpRequest httpRequest = (HttpRequest) request;
        URI rewrittenUri = fromUri(httpRequest.getURI())
                .path("/appended")
                .build(true)
                .toUri();
        httpRequest.setUri(rewrittenUri);
        return execution.execute(httpRequest, body);
    }
}
