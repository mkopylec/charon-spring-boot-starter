package com.github.mkopylec.charon.interceptors;

import java.io.IOException;

import com.github.mkopylec.charon.forwarding.CustomConfiguration;

import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import static com.github.mkopylec.charon.interceptors.RequestForwardingException.requestForwardingError;

public class HttpRequestExecution {

    private String forwardingName;
    private CustomConfiguration customConfiguration;
    private ClientHttpRequestExecution requestExecution;

    HttpRequestExecution(String forwardingName, CustomConfiguration customConfiguration, ClientHttpRequestExecution requestExecution) {
        this.forwardingName = forwardingName;
        this.customConfiguration = customConfiguration;
        this.requestExecution = requestExecution;
    }

    public HttpResponse execute(HttpRequest request) {
        try {
            ClientHttpResponse response = requestExecution.execute(request, request.getBody());
            return response instanceof HttpResponse
                    ? (HttpResponse) response
                    : new HttpResponse(response);
        } catch (IOException | RuntimeException e) {
            throw requestForwardingError("Error executing request: " + e.getMessage(), e);
        }
    }

    public String getForwardingName() {
        return forwardingName;
    }

    public <P> P getCustomProperty(String name) {
        return customConfiguration.getProperty(name);
    }
}
