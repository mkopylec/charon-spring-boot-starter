package com.github.mkopylec.charon.forwarding.interceptors;

import org.springframework.core.Ordered;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class HttpRequestInterceptor implements ClientHttpRequestInterceptor, Ordered {

    private String mappingName;
    private RequestForwardingInterceptor requestForwardingInterceptor;

    public HttpRequestInterceptor(String mappingName, RequestForwardingInterceptor requestForwardingInterceptor) {
        this.mappingName = mappingName;
        this.requestForwardingInterceptor = requestForwardingInterceptor;
    }

    @Override
    public ClientHttpResponse intercept(org.springframework.http.HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
        HttpRequest httpRequest = request instanceof HttpRequest
                ? (HttpRequest) request
                : new HttpRequest(request, body);
        HttpRequestExecution requestExecution = execution instanceof HttpRequestExecution
                ? (HttpRequestExecution) execution
                : new HttpRequestExecution(mappingName, execution);
        return requestForwardingInterceptor.forward(httpRequest, requestExecution);
    }

    @Override
    public int getOrder() {
        return requestForwardingInterceptor.getOrder();
    }
}
