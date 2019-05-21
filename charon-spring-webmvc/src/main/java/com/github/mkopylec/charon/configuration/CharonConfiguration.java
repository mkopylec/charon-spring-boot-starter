package com.github.mkopylec.charon.configuration;

import java.util.ArrayList;
import java.util.List;

import com.github.mkopylec.charon.forwarding.CustomConfiguration;
import com.github.mkopylec.charon.forwarding.RestTemplateConfiguration;
import com.github.mkopylec.charon.interceptors.RequestForwardingInterceptor;
import com.github.mkopylec.charon.interceptors.log.ForwardingLogger;

import static com.github.mkopylec.charon.configuration.RequestMappingConfigurer.requestMapping;
import static com.github.mkopylec.charon.forwarding.CustomConfigurer.custom;
import static com.github.mkopylec.charon.forwarding.RestTemplateConfigurer.restTemplate;
import static com.github.mkopylec.charon.interceptors.rewrite.AfterServerNameRequestHeadersRewriterConfigurer.afterServerNameRequestHeadersRewriter;
import static com.github.mkopylec.charon.interceptors.rewrite.BeforeServerNameRequestHeadersRewriterConfigurer.beforeServerNameRequestHeadersRewriter;
import static com.github.mkopylec.charon.interceptors.rewrite.RequestServerNameRewriterConfigurer.requestServerNameRewriter;
import static com.github.mkopylec.charon.interceptors.rewrite.ResponseHeadersRewriterConfigurer.responseHeadersRewriter;
import static com.github.mkopylec.charon.interceptors.rewrite.RootPathResponseCookieRewriterConfigurer.rootPathResponseCookieRewriter;
import static java.util.Collections.unmodifiableList;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

public class CharonConfiguration implements Valid {

    private int filterOrder;
    private RestTemplateConfiguration restTemplateConfiguration;
    private List<RequestForwardingInterceptor> requestForwardingInterceptors;
    private List<RequestMappingConfiguration> requestMappingConfigurations;
    private CustomConfiguration customConfiguration;

    CharonConfiguration() {
        filterOrder = LOWEST_PRECEDENCE;
        restTemplateConfiguration = restTemplate().configure();
        requestForwardingInterceptors = new ArrayList<>();
        addRequestForwardingInterceptor(new ForwardingLogger()); // TODO Why no configurer?
        addRequestForwardingInterceptor(beforeServerNameRequestHeadersRewriter().configure());
        addRequestForwardingInterceptor(requestServerNameRewriter().configure());
        addRequestForwardingInterceptor(afterServerNameRequestHeadersRewriter().configure());
        addRequestForwardingInterceptor(responseHeadersRewriter().configure());
        addRequestForwardingInterceptor(rootPathResponseCookieRewriter().configure());
        requestMappingConfigurations = new ArrayList<>();
        addRequestForwardingConfiguration(requestMapping("default").configure());
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

    List<RequestMappingConfiguration> getRequestMappingConfigurations() {
        return unmodifiableList(requestMappingConfigurations);
    }

    void addRequestForwardingConfiguration(RequestMappingConfiguration requestMappingConfiguration) {
        requestMappingConfigurations.add(requestMappingConfiguration);
    }

    void setCustomConfiguration(CustomConfiguration customConfiguration) {
        this.customConfiguration = customConfiguration;
    }

    private void mergeWithRequestForwardingConfigurations() {
        requestMappingConfigurations.forEach(configuration -> {
            configuration.mergeRestTemplateConfiguration(restTemplateConfiguration);
            configuration.mergeRequestForwardingInterceptors(requestForwardingInterceptors);
            configuration.mergeCustomConfiguration(customConfiguration);
        });
    }
}
