package com.github.mkopylec.charon.forwarding;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.time.Duration;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

class SimpleHttpRequestFactoryCreator implements ClientHttpRequestFactoryCreator {

    @Override
    public ClientHttpRequestFactory createRequestFactory(TimeoutConfiguration configuration) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory() {

            @Override // TODO Impossible to remove default headers, use okhttp?
            protected HttpURLConnection openConnection(URL url, Proxy proxy) throws IOException {
                HttpURLConnection connection = super.openConnection(url, proxy);
                connection.setRequestProperty("Accept", "wykop.pl"); // This actually works but it is not a solution
                return connection;
            }
        };
        requestFactory.setConnectTimeout(toMillis(configuration.getConnection()));
        requestFactory.setReadTimeout(toMillis(configuration.getRead()));
        return requestFactory;
    }

    private int toMillis(Duration duration) {
        return (int) duration.toMillis();
    }
}
