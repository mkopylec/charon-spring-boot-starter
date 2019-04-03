package com.github.mkopylec.charon.forwarding;

import java.time.Duration;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

class SimpleHttpRequestFactoryCreator implements ClientHttpRequestFactoryCreator {

    @Override
    public ClientHttpRequestFactory createRequestFactory(TimeoutConfiguration configuration) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(toMillis(configuration.getConnection()));
        requestFactory.setReadTimeout(toMillis(configuration.getRead()));
        return requestFactory;
    }

    private int toMillis(Duration duration) {
        return (int) duration.toMillis();
    }
}
