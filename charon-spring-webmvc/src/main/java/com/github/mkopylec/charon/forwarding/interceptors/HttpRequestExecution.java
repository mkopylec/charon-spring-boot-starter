package com.github.mkopylec.charon.forwarding.interceptors;

import java.io.IOException;

import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import static com.github.mkopylec.charon.forwarding.RequestForwardingException.requestForwardingError;

public class HttpRequestExecution {

    private String mappingName;
    private ClientHttpRequestExecution requestExecution;

    HttpRequestExecution(String mappingName, ClientHttpRequestExecution requestExecution) {
        this.mappingName = mappingName;
        this.requestExecution = requestExecution;
    }

    public HttpResponse execute(HttpRequest request) {
        try {
            ClientHttpResponse response = requestExecution.execute(request, request.getBody());
            return response instanceof HttpResponse
                    ? (HttpResponse) response
                    : new HttpResponse(response);
        } catch (IOException e) {
            throw requestForwardingError("Error executing request: " + e.getMessage(), e);
        }
    }

    public String getMappingName() {
        return mappingName;
    }
}
