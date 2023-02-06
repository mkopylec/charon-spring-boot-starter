package com.github.mkopylec.charon.forwarding;

import com.github.mkopylec.charon.configuration.Configurer;
import org.springframework.http.client.ClientHttpRequestInterceptor;

import java.util.List;

public class RestTemplateConfigurer extends Configurer<RestTemplateConfiguration> {

    private RestTemplateConfigurer() {
        super(new RestTemplateConfiguration());
    }

    public static RestTemplateConfigurer restTemplate() {
        return new RestTemplateConfigurer();
    }

    public RestTemplateConfigurer set(TimeoutConfigurer timeoutConfigurer) {
        configuredObject.setTimeoutConfiguration(timeoutConfigurer.configure());
        return this;
    }

    public RestTemplateConfigurer set(ClientHttpRequestFactoryCreatorConfigurer<?> clientHttpRequestFactoryCreatorConfigurer) {
        configuredObject.setClientHttpRequestFactoryCreator(clientHttpRequestFactoryCreatorConfigurer.configure());
        return this;
    }

    public RestTemplateConfigurer set(List<ClientHttpRequestInterceptor> clientHttpRequestInterceptors) {
        configuredObject.setClientHttpRequestInterceptors(clientHttpRequestInterceptors);
        return this;
    }
}
