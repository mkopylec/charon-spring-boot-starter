package com.github.mkopylec.charon.forwarding;

import org.springframework.http.client.ClientHttpRequestFactory;

public interface ClientHttpRequestFactoryCreator {

    ClientHttpRequestFactory createRequestFactory(TimeoutConfiguration configuration);
}
