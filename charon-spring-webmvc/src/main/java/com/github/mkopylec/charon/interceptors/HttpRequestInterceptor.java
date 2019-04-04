package com.github.mkopylec.charon.interceptors;

import com.github.mkopylec.charon.forwarding.CustomConfiguration;

import org.springframework.core.Ordered;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class HttpRequestInterceptor implements ClientHttpRequestInterceptor, Ordered {

    private String forwardingName;
    private CustomConfiguration customConfiguration;
    private RequestForwardingInterceptor requestForwardingInterceptor;

    public HttpRequestInterceptor(String forwardingName, CustomConfiguration customConfiguration, RequestForwardingInterceptor requestForwardingInterceptor) {
        this.forwardingName = forwardingName;
        this.customConfiguration = customConfiguration;
        this.requestForwardingInterceptor = requestForwardingInterceptor;
    }

    @Override
    public ClientHttpResponse intercept(org.springframework.http.HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
        HttpRequest httpRequest = request instanceof HttpRequest
                ? (HttpRequest) request
                : new HttpRequest(request, body);
        HttpRequestExecution requestExecution = execution instanceof HttpRequestExecution
                ? (HttpRequestExecution) execution
                : new HttpRequestExecution(forwardingName, customConfiguration, execution);
        return requestForwardingInterceptor.forward(httpRequest, requestExecution);
    }

    @Override
    public int getOrder() {
        return requestForwardingInterceptor.getOrder();
    }
}
