package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.Configurer;

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

    public WebClientConfigurer set(ClientHttpConnectorCreator clientHttpConnectorCreator) {
        configuredObject.setClientHttpConnectorCreator(clientHttpConnectorCreator);
        return this;
    }
}
