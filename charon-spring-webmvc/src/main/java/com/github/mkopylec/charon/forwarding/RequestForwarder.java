package com.github.mkopylec.charon.forwarding;

import java.util.List;

import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RequestForwarder {

    private int currentInterceptorIndex;
    private List<RequestForwardingInterceptor> interceptors;
    private HttpRequestMapper httpRequestMapper;
    private HttpClientProvider httpClientProvider;
    private HttpResponseMapper httpResponseMapper;

    RequestForwarder(List<RequestForwardingInterceptor> interceptors, HttpClientProvider httpClientProvider) {
        currentInterceptorIndex = -1;
        this.interceptors = interceptors;
        httpRequestMapper = new HttpRequestMapper();
        this.httpClientProvider = httpClientProvider;
        httpResponseMapper = new HttpResponseMapper();
    }

    public HttpResponse forward(HttpRequest request, RequestForwarding forwarding) {
        if (currentInterceptorIndex < interceptors.size() - 1) {
            currentInterceptorIndex++;
            return interceptors.get(currentInterceptorIndex).forward(request, forwarding, this);
        } else {
            RequestEntity<byte[]> requestEntity = httpRequestMapper.map(request);
            RestTemplate httpClient = httpClientProvider.getHttpClient(forwarding);
            // TODO Latency metrics, resilience4j does not provide one
            ResponseEntity<byte[]> responseEntity = httpClient.exchange(requestEntity, byte[].class);
            return httpResponseMapper.map(responseEntity);
        }
    }
}
