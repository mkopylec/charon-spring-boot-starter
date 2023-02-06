package com.github.mkopylec.charon.forwarding;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

import static com.github.mkopylec.charon.forwarding.Utils.toMillis;

class OkClientHttpRequestFactoryCreator implements ClientHttpRequestFactoryCreator {

    private OkHttpClient httpClient;

    OkClientHttpRequestFactoryCreator() {
        httpClient = new Builder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build();
    }

    @Override
    public ClientHttpRequestFactory createRequestFactory(TimeoutConfiguration configuration) {
        OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(toMillis(configuration.getConnection()));
        requestFactory.setReadTimeout(toMillis(configuration.getRead()));
        requestFactory.setWriteTimeout(toMillis(configuration.getWrite()));
        return requestFactory;
    }

    void setHttpClient(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
