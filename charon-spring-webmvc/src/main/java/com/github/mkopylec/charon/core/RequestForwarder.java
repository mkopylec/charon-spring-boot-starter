package com.github.mkopylec.charon.core;

import java.util.List;

import com.github.mkopylec.charon.core.interceptors.RequestForwardingInterceptor;

public class RequestForwarder {

    private int currentInterceptorIndex;
    private List<RequestForwardingInterceptor> interceptors;

    public RequestForwarder(List<RequestForwardingInterceptor> interceptors) {
        currentInterceptorIndex = -1;
        this.interceptors = interceptors;
    }

    public HttpResponse forward(HttpRequest request, RequestForwarding forwarding) {
        if (currentInterceptorIndex < interceptors.size() - 1) {
            currentInterceptorIndex++;
            return interceptors.get(currentInterceptorIndex).forward(request, forwarding, this);
        } else {
            // TODO Send request to outgoing server
            return null;
        }
    }
}
