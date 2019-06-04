package com.github.mkopylec.charon.configuration;

import com.github.mkopylec.charon.forwarding.CustomConfiguration;
import com.github.mkopylec.charon.forwarding.WebClientConfiguration;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;

import java.util.ArrayList;
import java.util.List;

import static com.github.mkopylec.charon.forwarding.CustomConfigurer.custom;
import static com.github.mkopylec.charon.forwarding.WebClientConfigurer.webClient;
import static com.github.mkopylec.charon.forwarding.interceptors.log.ForwardingLoggerConfigurer.forwardingLogger;
import static java.util.Collections.unmodifiableList;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

public class CharonConfiguration implements Valid {

    private int filterOrder;
    private WebClientConfiguration webClientConfiguration;
    private List<RequestForwardingInterceptor> requestForwardingInterceptors;
    private List<RequestMappingConfiguration> requestMappingConfigurations;
    private CustomConfiguration customConfiguration;

    CharonConfiguration() {
        filterOrder = HIGHEST_PRECEDENCE;
        webClientConfiguration = webClient().configure();
        requestForwardingInterceptors = new ArrayList<>();
        addRequestForwardingInterceptor(forwardingLogger().configure());
        requestMappingConfigurations = new ArrayList<>();
        customConfiguration = custom().configure();
    }

    int getFilterOrder() {
        return filterOrder;
    }

    void setFilterOrder(int filterOrder) {
        this.filterOrder = filterOrder;
    }

    void setWebClientConfiguration(WebClientConfiguration webClientConfiguration) {
        this.webClientConfiguration = webClientConfiguration;
    }

    void addRequestForwardingInterceptor(RequestForwardingInterceptor requestForwardingInterceptor) {
        removeRequestForwardingInterceptor(requestForwardingInterceptor.getOrder());
        requestForwardingInterceptors.add(requestForwardingInterceptor);
    }

    void removeRequestForwardingInterceptor(RequestForwardingInterceptorType requestForwardingInterceptorType) {
        removeRequestForwardingInterceptor(requestForwardingInterceptorType.getOrder());
    }

    List<RequestMappingConfiguration> getRequestMappingConfigurations() {
        return unmodifiableList(requestMappingConfigurations);
    }

    void addRequestForwardingConfiguration(RequestMappingConfiguration requestMappingConfiguration) {
        requestMappingConfigurations.add(requestMappingConfiguration);
    }

    void setCustomConfiguration(CustomConfiguration customConfiguration) {
        this.customConfiguration = customConfiguration;
    }

    void mergeWithRequestForwardingConfigurations() {
        requestMappingConfigurations.forEach(configuration -> {
            configuration.mergeRestTemplateConfiguration(webClientConfiguration);
            configuration.mergeRequestForwardingInterceptors(requestForwardingInterceptors);
            configuration.mergeCustomConfiguration(customConfiguration);
        });
    }

    private void removeRequestForwardingInterceptor(int interceptorOrder) {
        requestForwardingInterceptors.removeIf(interceptor -> interceptor.getOrder() == interceptorOrder);
    }
}
