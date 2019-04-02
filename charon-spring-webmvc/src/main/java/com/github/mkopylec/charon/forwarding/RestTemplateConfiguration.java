package com.github.mkopylec.charon.forwarding;

import java.util.List;
import java.util.function.Supplier;

import com.github.mkopylec.charon.configuration.RequestForwardingConfiguration;
import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.interceptors.HttpRequestInterceptor;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.Assert.notNull;

public class RestTemplateConfiguration implements Valid {

    private RestTemplateBuilder restTemplateBuilder;
    private ClientHttpRequestFactoryCreator clientHttpRequestFactoryCreator;

    public RestTemplateConfiguration(ClientHttpRequestFactoryCreator clientHttpRequestFactoryCreator) {
        restTemplateBuilder = new RestTemplateBuilder();
        this.clientHttpRequestFactoryCreator = clientHttpRequestFactoryCreator;
    }

    @Override
    public void validate() {
        notNull(restTemplateBuilder, "No rest template builder set");
        notNull(clientHttpRequestFactoryCreator, "No client HTTP request factory creator set");
    }

    void setConfiguration(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    RestTemplate configure(RequestForwardingConfiguration configuration) {
        Supplier<ClientHttpRequestFactory> requestFactory = getCreateRequestFactory(configuration.getTimeoutConfiguration());
        List<HttpRequestInterceptor> interceptors = createHttpRequestInterceptors(configuration);
        return restTemplateBuilder
                .requestFactory(requestFactory)
                .additionalInterceptors(interceptors)
                .build();
    }

    private Supplier<ClientHttpRequestFactory> getCreateRequestFactory(TimeoutConfiguration timeoutConfiguration) {
        return () -> clientHttpRequestFactoryCreator.createRequestFactory(timeoutConfiguration);
    }

    private List<HttpRequestInterceptor> createHttpRequestInterceptors(RequestForwardingConfiguration configuration) {
        return configuration.getRequestForwardingInterceptors().stream()
                .map(interceptor -> new HttpRequestInterceptor(configuration.getName(), configuration.getCustomConfiguration(), interceptor))
                .collect(toList());
    }
}
