package com.github.mkopylec.charon.configuration;

import java.util.ArrayList;
import java.util.List;

import com.github.mkopylec.charon.forwarding.CustomConfiguration;
import com.github.mkopylec.charon.forwarding.RestTemplateConfiguration;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;
import com.github.mkopylec.charon.interceptors.log.Logger;

import static com.github.mkopylec.charon.configuration.RequestForwardingConfigurer.requestForwarding;
import static com.github.mkopylec.charon.forwarding.CustomConfigurer.custom;
import static com.github.mkopylec.charon.forwarding.RestTemplateConfigurer.restTemplate;
import static com.github.mkopylec.charon.interceptors.rewrite.RootPathResponseCookieRewriterConfigurer.rootPathResponseCookieRewriter;
import static java.util.Collections.unmodifiableList;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

public class CharonConfiguration implements Valid {

    private int filterOrder;
    private RestTemplateConfiguration restTemplateConfiguration;
    private List<RequestForwardingInterceptor> requestForwardingInterceptors;
    private List<RequestForwardingConfiguration> requestForwardingConfigurations;
    private CustomConfiguration customConfiguration;

    CharonConfiguration() {
        filterOrder = LOWEST_PRECEDENCE;
        restTemplateConfiguration = restTemplate().configure();
        requestForwardingInterceptors = new ArrayList<>();
        addRequestForwardingInterceptor(new Logger());
        addRequestForwardingInterceptor(rootPathResponseCookieRewriter().configure());
        // TODO Think about more default interceptors
        requestForwardingConfigurations = new ArrayList<>();
        addRequestForwardingConfiguration(requestForwarding("default").configure());
        customConfiguration = custom().configure();
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
        requestForwardingInterceptors.removeIf(interceptor -> interceptor.getOrder() == requestForwardingInterceptor.getOrder());
        requestForwardingInterceptors.add(requestForwardingInterceptor);
    }

    List<RequestForwardingConfiguration> getRequestForwardingConfigurations() {
        return unmodifiableList(requestForwardingConfigurations);
    }

    void addRequestForwardingConfiguration(RequestForwardingConfiguration requestForwardingConfiguration) {
        requestForwardingConfigurations.add(requestForwardingConfiguration);
    }

    void setCustomConfiguration(CustomConfiguration customConfiguration) {
        this.customConfiguration = customConfiguration;
    }

    private void mergeWithRequestForwardingConfigurations() {
        requestForwardingConfigurations.forEach(configuration -> {
            configuration.mergeRestTemplateConfiguration(restTemplateConfiguration);
            configuration.mergeRequestForwardingInterceptors(requestForwardingInterceptors);
            configuration.mergeCustomConfiguration(customConfiguration);
        });
    }
}
