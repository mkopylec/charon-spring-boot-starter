package com.github.mkopylec.charon.configuration;

import java.util.ArrayList;
import java.util.List;

import com.github.mkopylec.charon.forwarding.WebClientConfiguration;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptor;
import com.github.mkopylec.charon.forwarding.interceptors.RequestForwardingInterceptorType;

import static com.github.mkopylec.charon.forwarding.WebClientConfigurer.webClient;
import static com.github.mkopylec.charon.forwarding.interceptors.log.ForwardingLoggerConfigurer.forwardingLogger;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestProtocolHeadersRewriterConfigurer.requestProtocolHeadersRewriter;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RequestProxyHeadersRewriterConfigurer.requestProxyHeadersRewriter;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.ResponseProtocolHeadersRewriterConfigurer.responseProtocolHeadersRewriter;
import static com.github.mkopylec.charon.forwarding.interceptors.rewrite.RootPathResponseCookiesRewriterConfigurer.rootPathResponseCookiesRewriter;
import static java.util.Collections.unmodifiableList;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

public class CharonConfiguration implements Valid {

    private int filterOrder;
    private WebClientConfiguration webClientConfiguration;
    private List<RequestForwardingInterceptor> requestForwardingInterceptors;
    private List<RequestMappingConfiguration> requestMappingConfigurations;

    CharonConfiguration() {
        filterOrder = HIGHEST_PRECEDENCE;
        webClientConfiguration = webClient().configure();
        requestForwardingInterceptors = new ArrayList<>();
        addRequestForwardingInterceptor(forwardingLogger().configure());
        addRequestForwardingInterceptor(requestProtocolHeadersRewriter().configure());
        addRequestForwardingInterceptor(requestProxyHeadersRewriter().configure());
        addRequestForwardingInterceptor(responseProtocolHeadersRewriter().configure());
        addRequestForwardingInterceptor(rootPathResponseCookiesRewriter().configure());
        requestMappingConfigurations = new ArrayList<>();
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
        removeRequestForwardingInterceptor(requestForwardingInterceptor.getType());
        requestForwardingInterceptors.add(requestForwardingInterceptor);
    }

    void removeRequestForwardingInterceptor(RequestForwardingInterceptorType requestForwardingInterceptorType) {
        requestForwardingInterceptors.removeIf(interceptor -> interceptor.getType().equals(requestForwardingInterceptorType));
    }

    List<RequestMappingConfiguration> getRequestMappingConfigurations() {
        return unmodifiableList(requestMappingConfigurations);
    }

    void addRequestForwardingConfiguration(RequestMappingConfiguration requestMappingConfiguration) {
        requestMappingConfigurations.add(requestMappingConfiguration);
    }

    void mergeWithRequestForwardingConfigurations() {
        requestMappingConfigurations.forEach(configuration -> {
            configuration.mergeRestTemplateConfiguration(webClientConfiguration);
            configuration.mergeRequestForwardingInterceptors(requestForwardingInterceptors);
        });
    }
}
