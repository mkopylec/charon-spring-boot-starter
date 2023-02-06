package com.github.mkopylec.charon.test;

import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

import java.util.List;

import static org.springframework.web.reactive.function.client.ExchangeStrategies.builder;

class DataBufferSizeSetter implements ExchangeStrategies {

    private ExchangeStrategies delegate = builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1))
            .build();

    @Override
    public List<HttpMessageReader<?>> messageReaders() {
        return delegate.messageReaders();
    }

    @Override
    public List<HttpMessageWriter<?>> messageWriters() {
        return delegate.messageWriters();
    }

    @Override
    public Builder mutate() {
        return delegate.mutate();
    }
}
