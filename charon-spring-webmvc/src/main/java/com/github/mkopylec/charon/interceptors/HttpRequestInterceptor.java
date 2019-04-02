package com.github.mkopylec.charon.interceptors;

import java.io.IOException;

import com.github.mkopylec.charon.forwarding.CustomConfiguration;

import org.springframework.core.Ordered;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class HttpRequestInterceptor implements ClientHttpRequestInterceptor, Ordered {

    private String name;
    private CustomConfiguration customConfiguration;
    private RequestForwardingInterceptor requestForwardingInterceptor;

    public HttpRequestInterceptor(String name, CustomConfiguration customConfiguration, RequestForwardingInterceptor requestForwardingInterceptor) {
        this.name = name;
        this.customConfiguration = customConfiguration;
        this.requestForwardingInterceptor = requestForwardingInterceptor;
    }

    @Override
    public ClientHttpResponse intercept(org.springframework.http.HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpRequest httpRequest = request instanceof HttpRequest
                ? (HttpRequest) request
                : new HttpRequest(request, body, name, customConfiguration);
        HttpRequestExecution requestExecution = new HttpRequestExecution(execution);
        return requestForwardingInterceptor.forward(httpRequest, requestExecution);
    }

    @Override
    public int getOrder() {
        return requestForwardingInterceptor.getOrder();
    }
}
