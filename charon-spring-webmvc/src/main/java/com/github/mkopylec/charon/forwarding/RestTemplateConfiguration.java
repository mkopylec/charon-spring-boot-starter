package com.github.mkopylec.charon.forwarding;

import java.util.List;
import java.util.function.Supplier;

import com.github.mkopylec.charon.configuration.RequestForwardingConfiguration;
import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.interceptors.HttpRequestInterceptor;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static com.github.mkopylec.charon.forwarding.TimeoutConfigurer.timeout;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.Assert.notNull;

public class RestTemplateConfiguration implements Valid {

    private TimeoutConfiguration timeoutConfiguration;
    private ClientHttpRequestFactoryCreator clientHttpRequestFactoryCreator;

    RestTemplateConfiguration() {
        this.timeoutConfiguration = timeout().configure();
        this.clientHttpRequestFactoryCreator = new SimpleHttpRequestFactoryCreator();
    }

    @Override
    public void validate() {
        notNull(clientHttpRequestFactoryCreator, "No client HTTP request factory creator set");
    }

    void setTimeoutConfiguration(TimeoutConfiguration timeoutConfiguration) {
        this.timeoutConfiguration = timeoutConfiguration;
    }

    void setClientHttpRequestFactoryCreator(ClientHttpRequestFactoryCreator clientHttpRequestFactoryCreator) {
        this.clientHttpRequestFactoryCreator = clientHttpRequestFactoryCreator;
    }

    RestTemplate configure(RequestForwardingConfiguration configuration) {
        Supplier<ClientHttpRequestFactory> requestFactory = getCreateRequestFactory(timeoutConfiguration);
        List<HttpRequestInterceptor> interceptors = createHttpRequestInterceptors(configuration);
        return new RestTemplateBuilder()
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
