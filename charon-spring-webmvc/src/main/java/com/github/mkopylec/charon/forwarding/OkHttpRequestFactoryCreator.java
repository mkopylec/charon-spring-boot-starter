package com.github.mkopylec.charon.forwarding;

import java.time.Duration;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

class OkHttpRequestFactoryCreator implements ClientHttpRequestFactoryCreator {

    @Override
    public ClientHttpRequestFactory createRequestFactory(TimeoutConfiguration configuration) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(0, 1, MILLISECONDS))
                .followRedirects(false)
                .followSslRedirects(false)
                .build();
        OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory(client);
        requestFactory.setConnectTimeout(toMillis(configuration.getConnection()));
        requestFactory.setReadTimeout(toMillis(configuration.getRead()));
        requestFactory.setWriteTimeout(toMillis(configuration.getWrite()));
        return requestFactory;
    }

    private int toMillis(Duration duration) {
        return (int) duration.toMillis();
    }
}
