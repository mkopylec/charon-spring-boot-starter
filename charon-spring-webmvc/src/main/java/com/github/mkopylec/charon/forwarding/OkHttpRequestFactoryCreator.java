package com.github.mkopylec.charon.forwarding;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

import static com.github.mkopylec.charon.forwarding.Utils.toMillis;

class OkHttpRequestFactoryCreator implements ClientHttpRequestFactoryCreator {

    @Override
    public ClientHttpRequestFactory createRequestFactory(TimeoutConfiguration configuration) {
        OkHttpClient client = new Builder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build();
        OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory(client);
        requestFactory.setConnectTimeout(toMillis(configuration.getConnection()));
        requestFactory.setReadTimeout(toMillis(configuration.getRead()));
        requestFactory.setWriteTimeout(toMillis(configuration.getWrite()));
        return requestFactory;
    }
}
