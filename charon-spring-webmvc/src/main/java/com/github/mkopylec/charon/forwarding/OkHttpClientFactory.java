package com.github.mkopylec.charon.forwarding;

import java.time.Duration;

import com.github.mkopylec.charon.configuration.TimeoutConfiguration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

class OkHttpClientFactory implements HttpClientFactory {

    @Override
    public RestTemplate createHttpClient(RequestForwarding forwarding) {
        TimeoutConfiguration timeoutConfiguration = forwarding.getTimeoutConfiguration();
        OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();
        requestFactory.setConnectTimeout(toMillis(timeoutConfiguration.getConnection()));
        requestFactory.setReadTimeout(toMillis(timeoutConfiguration.getRead()));
        requestFactory.setWriteTimeout(toMillis(timeoutConfiguration.getWrite()));
        return new RestTemplateBuilder()
                .requestFactory(() -> requestFactory)
                .build();
    }

    private int toMillis(Duration duration) {
        return (int) duration.toMillis();
    }
}
