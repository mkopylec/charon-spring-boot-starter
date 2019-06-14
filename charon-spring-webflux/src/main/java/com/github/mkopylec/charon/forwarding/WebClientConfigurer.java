package com.github.mkopylec.charon.forwarding;

import java.util.List;

import com.github.mkopylec.charon.configuration.Configurer;

import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

public class WebClientConfigurer extends Configurer<WebClientConfiguration> {

    private WebClientConfigurer() {
        super(new WebClientConfiguration());
    }

    public static WebClientConfigurer webClient() {
        return new WebClientConfigurer();
    }

    public WebClientConfigurer set(TimeoutConfigurer timeoutConfigurer) {
        configuredObject.setTimeoutConfiguration(timeoutConfigurer.configure());
        return this;
    }

    public WebClientConfigurer set(ClientHttpConnectorCreatorConfigurer<?> clientHttpConnectorCreatorConfigurer) {
        configuredObject.setClientHttpConnectorCreator(clientHttpConnectorCreatorConfigurer.configure());
        return this;
    }

    public WebClientConfigurer set(List<ExchangeFilterFunction> exchangeFilterFunctions) {
        configuredObject.setExchangeFilterFunctions(exchangeFilterFunctions);
        return this;
    }
}
