package com.github.mkopylec.charon.forwarding;

import java.util.ArrayList;
import java.util.List;

import com.github.mkopylec.charon.configuration.RequestMappingConfiguration;
import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestInterceptor;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import static com.github.mkopylec.charon.forwarding.OkClientHttpRequestFactoryCreatorConfigurer.okClientHttpRequestFactoryCreator;
import static com.github.mkopylec.charon.forwarding.TimeoutConfigurer.timeout;
import static java.util.stream.Collectors.toList;

public class RestTemplateConfiguration implements Valid {

    private TimeoutConfiguration timeoutConfiguration;
    private ClientHttpRequestFactoryCreator clientHttpRequestFactoryCreator;

    RestTemplateConfiguration() {
        this.timeoutConfiguration = timeout().configure();
        this.clientHttpRequestFactoryCreator = okClientHttpRequestFactoryCreator().configure();
    }

    void setTimeoutConfiguration(TimeoutConfiguration timeoutConfiguration) {
        this.timeoutConfiguration = timeoutConfiguration;
    }

    void setClientHttpRequestFactoryCreator(ClientHttpRequestFactoryCreator clientHttpRequestFactoryCreator) {
        this.clientHttpRequestFactoryCreator = clientHttpRequestFactoryCreator;
    }

    RestTemplate configure(RequestMappingConfiguration configuration) {
        ClientHttpRequestFactory requestFactory = clientHttpRequestFactoryCreator.createRequestFactory(timeoutConfiguration);
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(createHttpRequestInterceptors(configuration));
        RestTemplate restTemplate = new RetryAwareRestTemplate();
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(new NoExceptionErrorHandler());
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    private List<HttpRequestInterceptor> createHttpRequestInterceptors(RequestMappingConfiguration configuration) {
        return configuration.getRequestForwardingInterceptors().stream()
                .map(interceptor -> new HttpRequestInterceptor(configuration.getName(), configuration.getCustomConfiguration(), interceptor))
                .collect(toList());
    }
}
