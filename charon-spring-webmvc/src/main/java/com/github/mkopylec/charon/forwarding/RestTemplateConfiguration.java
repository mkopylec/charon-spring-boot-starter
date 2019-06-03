package com.github.mkopylec.charon.forwarding;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.github.mkopylec.charon.configuration.RequestMappingConfiguration;
import com.github.mkopylec.charon.configuration.Valid;
import com.github.mkopylec.charon.forwarding.interceptors.HttpRequestInterceptor;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import static com.github.mkopylec.charon.forwarding.TimeoutConfigurer.timeout;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.Assert.notNull;

public class RestTemplateConfiguration implements Valid {

    private TimeoutConfiguration timeoutConfiguration;
    private ClientHttpRequestFactoryCreator clientHttpRequestFactoryCreator;

    RestTemplateConfiguration() {
        this.timeoutConfiguration = timeout().configure();
        this.clientHttpRequestFactoryCreator = new OkHttpRequestFactoryCreator();
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

    RestTemplate configure(RequestMappingConfiguration configuration) {
        Supplier<ClientHttpRequestFactory> requestFactory = createRequestFactory(timeoutConfiguration);
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(createHttpRequestInterceptors(configuration));
        // TODO Retryer is not working, because of InterceptingClientHttpRequest:90. Need to implement custom Iterator over interceptors. Use ThreadLocal to store next interceptor index? Webflux should work correctly without any modification?
        return new RestTemplateBuilder()
                .requestFactory(requestFactory)
                .errorHandler(new NoExceptionErrorHandler())
                .additionalInterceptors(interceptors)
                .build();
    }

    private Supplier<ClientHttpRequestFactory> createRequestFactory(TimeoutConfiguration timeoutConfiguration) {
        return () -> clientHttpRequestFactoryCreator.createRequestFactory(timeoutConfiguration);
    }

    private List<HttpRequestInterceptor> createHttpRequestInterceptors(RequestMappingConfiguration configuration) {
        return configuration.getRequestForwardingInterceptors().stream()
                .map(interceptor -> new HttpRequestInterceptor(configuration.getName(), configuration.getCustomConfiguration(), interceptor))
                .collect(toList());
    }
}
