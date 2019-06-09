package com.github.mkopylec.charon.forwarding;

import java.util.ArrayList;
import java.util.List;

import com.github.mkopylec.charon.configuration.RequestMappingConfiguration;
import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestInterceptor;

import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import static com.github.mkopylec.charon.forwarding.ReactorClientHttpConnectorCreatorConfigurer.reactorClientHttpConnectorCreator;
import static com.github.mkopylec.charon.forwarding.TimeoutConfigurer.timeout;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.reactive.function.client.WebClient.builder;

public class WebClientConfiguration implements Valid {

    private TimeoutConfiguration timeoutConfiguration;
    private ClientHttpConnectorCreator clientHttpConnectorCreator;

    WebClientConfiguration() {
        this.timeoutConfiguration = timeout().configure();
        this.clientHttpConnectorCreator = reactorClientHttpConnectorCreator().configure();
    }

    void setTimeoutConfiguration(TimeoutConfiguration timeoutConfiguration) {
        this.timeoutConfiguration = timeoutConfiguration;
    }

    void setClientHttpConnectorCreator(ClientHttpConnectorCreator clientHttpConnectorCreator) {
        this.clientHttpConnectorCreator = clientHttpConnectorCreator;
    }

    WebClient configure(RequestMappingConfiguration configuration) {
        ClientHttpConnector connector = clientHttpConnectorCreator.createConnector(timeoutConfiguration);
        List<ExchangeFilterFunction> interceptors = new ArrayList<>(createHttpRequestInterceptors(configuration));
        return builder()
                .clientConnector(connector)
                .filters(filters -> filters.addAll(interceptors))
                .build();
    }

    private List<HttpRequestInterceptor> createHttpRequestInterceptors(RequestMappingConfiguration configuration) {
        return configuration.getRequestForwardingInterceptors().stream()
                .map(interceptor -> new HttpRequestInterceptor(configuration.getName(), configuration.getCustomConfiguration(), interceptor))
                .collect(toList());
    }
}
