package com.github.mkopylec.charon.forwarding;

import java.util.List;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

class RetryAwareRestTemplate extends RestTemplate {

    private List<ClientHttpRequestInterceptor> interceptors = new RetryAwareList<>();

    @Override
    public void setInterceptors(List<ClientHttpRequestInterceptor> interceptors) {
        this.interceptors.clear();
        this.interceptors.addAll(interceptors);
    }

    @Override
    public List<ClientHttpRequestInterceptor> getInterceptors() {
        return interceptors;
    }
}
