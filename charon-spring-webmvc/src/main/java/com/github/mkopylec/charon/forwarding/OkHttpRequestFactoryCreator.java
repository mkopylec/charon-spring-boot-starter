package com.github.mkopylec.charon.forwarding;

import java.time.Duration;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

class OkHttpRequestFactoryCreator implements ClientHttpRequestFactoryCreator {

    @Override
    public ClientHttpRequestFactory createRequestFactory(TimeoutConfiguration configuration) {
        OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();
        requestFactory.setConnectTimeout(toMillis(configuration.getConnection()));
        requestFactory.setReadTimeout(toMillis(configuration.getRead()));
        requestFactory.setWriteTimeout(toMillis(configuration.getWrite()));
        return requestFactory;
    }

    private int toMillis(Duration duration) {
        return (int) duration.toMillis();
    }
}
