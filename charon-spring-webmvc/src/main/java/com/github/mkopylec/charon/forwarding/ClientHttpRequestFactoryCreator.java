package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.Valid;
import org.springframework.http.client.ClientHttpRequestFactory;

public interface ClientHttpRequestFactoryCreator extends Valid {

    ClientHttpRequestFactory createRequestFactory(TimeoutConfiguration configuration);
}
