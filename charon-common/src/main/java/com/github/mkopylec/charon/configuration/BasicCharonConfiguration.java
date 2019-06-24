package com.github.mkopylec.charon.configuration;

import java.util.ArrayList;
import java.util.List;

import com.github.mkopylec.charon.forwarding.BasicRequestForwardingInterceptor;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;

import static java.util.Collections.unmodifiableList;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

public abstract class BasicCharonConfiguration implements Valid {

    private int filterOrder;
    private List<BasicRequestForwardingInterceptor> requestForwardingInterceptors;
    private List<BasicRequestMappingConfiguration> requestMappingConfigurations;

    BasicCharonConfiguration() {
        filterOrder = HIGHEST_PRECEDENCE;
        requestForwardingInterceptors = new ArrayList<>();
        requestMappingConfigurations = new ArrayList<>();
    }

    int getFilterOrder() {
        return filterOrder;
    }

    void setFilterOrder(int filterOrder) {
        this.filterOrder = filterOrder;
    }

    void addRequestForwardingInterceptor(BasicRequestForwardingInterceptor requestForwardingInterceptor) {
        removeRequestForwardingInterceptor(requestForwardingInterceptor.getType());
        requestForwardingInterceptors.add(requestForwardingInterceptor);
    }

    void removeRequestForwardingInterceptor(RequestForwardingInterceptorType requestForwardingInterceptorType) {
        requestForwardingInterceptors.removeIf(interceptor -> interceptor.getType().equals(requestForwardingInterceptorType));
    }

    List<BasicRequestMappingConfiguration> getRequestMappingConfigurations() {
        return unmodifiableList(requestMappingConfigurations);
    }

    BasicRequestMappingConfigurer<?, ?> getRequestMappingConfigurer(String requestMappingName) {
        return requestMappingConfigurations.stream()
                .filter(configuration -> configuration.getName().equals(requestMappingName))
                .findFirst()
                .map(BasicRequestMappingConfiguration::getRequestMappingConfigurer)
                .orElse(null);
    }

    void addRequestMappingConfiguration(BasicRequestMappingConfiguration requestMappingConfiguration) {
        requestMappingConfigurations.remove(requestMappingConfiguration);
        requestMappingConfigurations.add(requestMappingConfiguration);
    }

    void mergeWithRequestMappingConfigurations() {
        requestMappingConfigurations.forEach(configuration -> configuration.mergeRequestForwardingInterceptors(requestForwardingInterceptors));
    }
}
