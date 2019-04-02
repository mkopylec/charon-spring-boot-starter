package com.github.mkopylec.charon.interceptors;

import java.io.IOException;

import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

public class HttpRequestExecution {

    private ClientHttpRequestExecution requestExecution;

    HttpRequestExecution(ClientHttpRequestExecution requestExecution) {
        this.requestExecution = requestExecution;
    }

    public HttpResponse execute(HttpRequest request) throws IOException {
        ClientHttpResponse response = requestExecution.execute(request, request.getBody());
        return response instanceof HttpResponse
                ? (HttpResponse) response
                : new HttpResponse(response);
    }
}
