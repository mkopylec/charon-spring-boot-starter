package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.Configurer;

public abstract class HttpClientFactoryConfigurer<F extends HttpClientFactory> extends Configurer<F> {

    protected HttpClientFactoryConfigurer(F configuredObject) {
        super(configuredObject);
    }
}
