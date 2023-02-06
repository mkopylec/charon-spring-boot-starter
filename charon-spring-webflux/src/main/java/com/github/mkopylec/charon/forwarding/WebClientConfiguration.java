package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.RequestMappingConfiguration;
import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestInterceptor;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

import static com.github.mkopylec.charon.forwarding.ReactorClientHttpConnectorCreatorConfigurer.reactorClientHttpConnectorCreator;
import static com.github.mkopylec.charon.forwarding.TimeoutConfigurer.timeout;
import static java.util.stream.Collectors.toList;
import static org.springframework.web.reactive.function.client.WebClient.builder;

public class WebClientConfiguration implements Valid {

    private TimeoutConfiguration timeoutConfiguration;
    private ClientHttpConnectorCreator clientHttpConnectorCreator;
    private List<ExchangeFilterFunction> exchangeFilterFunctions;
    private ExchangeStrategies exchangeStrategies;

    WebClientConfiguration() {
        this.timeoutConfiguration = timeout().configure();
        this.clientHttpConnectorCreator = reactorClientHttpConnectorCreator().configure();
        exchangeFilterFunctions = new ArrayList<>();
    }

    void setTimeoutConfiguration(TimeoutConfiguration timeoutConfiguration) {
        this.timeoutConfiguration = timeoutConfiguration;
    }

    void setClientHttpConnectorCreator(ClientHttpConnectorCreator clientHttpConnectorCreator) {
        this.clientHttpConnectorCreator = clientHttpConnectorCreator;
    }

    void setExchangeFilterFunctions(List<ExchangeFilterFunction> exchangeFilterFunctions) {
        this.exchangeFilterFunctions.addAll(exchangeFilterFunctions);
    }

    void setExchangeStrategies(ExchangeStrategies exchangeStrategies) {
        this.exchangeStrategies = exchangeStrategies;
    }

    WebClient configure(RequestMappingConfiguration configuration) {
        ClientHttpConnector connector = clientHttpConnectorCreator.createConnector(timeoutConfiguration);
        List<ExchangeFilterFunction> interceptors = new ArrayList<>(createHttpRequestInterceptors(configuration));
        interceptors.addAll(exchangeFilterFunctions);
        return builder()
                .clientConnector(connector)
                .filters(filters -> filters.addAll(interceptors))
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

    private List<HttpRequestInterceptor> createHttpRequestInterceptors(RequestMappingConfiguration configuration) {
        return configuration.getRequestForwardingInterceptors().stream()
                .map(interceptor -> new HttpRequestInterceptor(configuration.getName(), interceptor))
                .collect(toList());
    }
}
