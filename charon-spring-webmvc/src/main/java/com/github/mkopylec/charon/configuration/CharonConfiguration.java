package com.github.mkopylec.charon.configuration;

import java.util.ArrayList;
import java.util.List;

import com.github.mkopylec.charon.forwarding.RestTemplateConfiguration;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;

import static com.github.mkopylec.charon.forwarding.RestTemplateConfigurer.restTemplate;
import static com.github.mkopylec.charon.forwarding.interceptors.log.ForwardingLoggerConfigurer.forwardingLogger;
import static java.util.Collections.unmodifiableList;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

public class CharonConfiguration implements Valid {

    private int filterOrder;
    private RestTemplateConfiguration restTemplateConfiguration;
    private List<RequestForwardingInterceptor> requestForwardingInterceptors;
    private List<RequestMappingConfiguration> requestMappingConfigurations;

    CharonConfiguration() {
        filterOrder = HIGHEST_PRECEDENCE;
        restTemplateConfiguration = restTemplate().configure();
        requestForwardingInterceptors = new ArrayList<>();
        addRequestForwardingInterceptor(forwardingLogger().configure());
        requestMappingConfigurations = new ArrayList<>();
    }

    int getFilterOrder() {
        return filterOrder;
    }

    void setFilterOrder(int filterOrder) {
        this.filterOrder = filterOrder;
    }

    void setRestTemplateConfiguration(RestTemplateConfiguration restTemplateConfiguration) {
        this.restTemplateConfiguration = restTemplateConfiguration;
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

    void mergeWithRequestForwardingConfigurations() {
        requestMappingConfigurations.forEach(configuration -> {
            configuration.mergeRestTemplateConfiguration(restTemplateConfiguration);
            configuration.mergeRequestForwardingInterceptors(requestForwardingInterceptors);
        });
    }

    private void removeRequestForwardingInterceptor(int interceptorOrder) {
        requestForwardingInterceptors.removeIf(interceptor -> interceptor.getOrder() == interceptorOrder);
    }
}
